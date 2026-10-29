FROM nginx:1.30.6-alpine@sha256:0e52bd054126cdc040b1276c0af347aeb02a41b9cfe305d6094767ac30e97949

RUN apk upgrade --no-cache

COPY teavm/build/dist/webapp /usr/share/nginx/html
COPY teavm/src/main/webapp/index.html /usr/share/nginx/html/index.html

COPY teavm/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=3s CMD wget -q --spider http://127.0.0.1/
