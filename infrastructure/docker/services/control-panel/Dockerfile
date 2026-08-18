FROM node:22-alpine AS build
WORKDIR /app
COPY frontend/control-panel/package.json ./
RUN npm install
COPY frontend/control-panel/ ./
RUN npm run build

FROM nginx:1.27-alpine
COPY infrastructure/docker/services/control-panel/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80

