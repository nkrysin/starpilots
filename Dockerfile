FROM nginx:1.30.7-alpine@sha256:bef154735288bc38d99fac41ae63730684bcc661503673a04a0f9bc526ba8598

RUN apk upgrade --no-cache

COPY teavm/build/dist/webapp /usr/share/nginx/html
COPY teavm/src/main/webapp/index.html /usr/share/nginx/html/index.html

COPY teavm/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=3s CMD wget -q --spider http://127.0.0.1/
