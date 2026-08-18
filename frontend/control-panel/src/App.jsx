import { useEffect, useMemo, useState } from "react";

const PROFILES = ["SMALL", "MEDIUM", "LARGE"];

async function fetchBots() {
  const response = await fetch("/api/bots");
  if (!response.ok) {
    throw new Error(`Could not load bots (${response.status})`);
  }
  return response.json();
}

async function sendLifecycle(botId, action, profile) {
  const response = await fetch(`/api/bots/${botId}/lifecycle`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      action,
      profile,
      requestedBy: "control-panel"
    })
  });

  if (!response.ok) {
    let reasonCode = "";
    try {
      const payload = await response.json();
      reasonCode = payload.errorCode || "";
    } catch {
      // Keep fallback below when backend returns no JSON body.
    }

    if (reasonCode) {
      throw new Error(`Could not trigger ${action} for ${botId} (${reasonCode})`);
    }
    throw new Error(`Could not trigger ${action} for ${botId}`);
  }
}

function statusClass(status) {
  if (status === "START" || status === "ONLINE") return "status-online";
  if (status === "STOP" || status === "OFFLINE") return "status-offline";
  return "status-unknown";
}

export default function App() {
  const [bots, setBots] = useState([]);
  const [profiles, setProfiles] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [busyBotId, setBusyBotId] = useState("");

  const initializedProfiles = useMemo(() => {
    const next = {};
    for (const bot of bots) {
      next[bot.botId] = profiles[bot.botId] || bot.defaultProfile || "MEDIUM";
    }
    return next;
  }, [bots, profiles]);

  async function loadBots() {
    setLoading(true);
    setError("");
    try {
      const data = await fetchBots();
      setBots(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadBots();
    const id = setInterval(loadBots, 5000);
    return () => clearInterval(id);
  }, []);

  async function trigger(botId, action) {
    setBusyBotId(botId);
    setError("");
    try {
      await sendLifecycle(botId, action, initializedProfiles[botId]);
      await loadBots();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setBusyBotId("");
    }
  }

  return (
    <main className="page">
      <header className="topbar">
        <h1>Bot Control Panel</h1>
        <button type="button" onClick={loadBots} disabled={loading}>
          Refresh
        </button>
      </header>

      {error ? <p className="error">{error}</p> : null}

      {loading ? <p className="hint">Loading bots...</p> : null}

      <section className="grid">
        {bots.map((bot) => {
          const profile = initializedProfiles[bot.botId] || "MEDIUM";
          const status = bot.liveStatus || "UNKNOWN";
          const busy = busyBotId === bot.botId;

          return (
            <article key={bot.botId} className="card">
              <div className="card-head">
                <h2>{bot.displayName}</h2>
                <span className={`status ${statusClass(status)}`}>{status}</span>
              </div>

              <p className="muted">ID: {bot.botId}</p>
              <p className="muted">Capabilities: {(bot.capabilities || []).join(", ") || "-"}</p>
              <p className="muted">Configured: {bot.configured ? "yes" : "no"}</p>
              <p className="muted">Runnable: {bot.runnable ? "yes" : "no"}</p>
              {!bot.runnable && bot.reasonCode ? <p className="muted">Reason: {bot.reasonCode}</p> : null}

              <label className="row" htmlFor={`profile-${bot.botId}`}>
                Profile
                <select
                  id={`profile-${bot.botId}`}
                  value={profile}
                  onChange={(event) => {
                    setProfiles((prev) => ({ ...prev, [bot.botId]: event.target.value }));
                  }}
                >
                  {PROFILES.map((entry) => (
                    <option key={entry} value={entry}>
                      {entry}
                    </option>
                  ))}
                </select>
              </label>

              <div className="actions">
                <button type="button" disabled={busy || !bot.runnable} onClick={() => trigger(bot.botId, "START")}>
                  Start
                </button>
                <button type="button" className="secondary" disabled={busy} onClick={() => trigger(bot.botId, "STOP")}>
                  Stop
                </button>
              </div>
            </article>
          );
        })}
      </section>
    </main>
  );
}

