# ---- Stage 1: React build ----
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
# Browser /gw/ pe request bhejega, nginx use gateway tak pahuchayega
ARG REACT_APP_API_URL=/gw
ENV REACT_APP_API_URL=$REACT_APP_API_URL
ENV CI=false
RUN npm run build

# ---- Stage 2: nginx se serve ----
FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
