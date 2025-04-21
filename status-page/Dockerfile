FROM nginx:alpine

COPY status-page.html /usr/share/nginx/html/status.html

RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf