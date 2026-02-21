FROM nginx:alpine

COPY teavm/build/dist/webapp /usr/share/nginx/html
COPY teavm/src/main/webapp/index.html /usr/share/nginx/html/index.html

COPY teavm/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
