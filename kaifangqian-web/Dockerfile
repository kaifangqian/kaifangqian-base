FROM registry.cn-beijing.aliyuncs.com/kaifangqian/nginx:1.26.1

ADD ./opensign-web/dist /data/
ADD ./opensign-tenant/dist /data/opensign-web
ADD ./opensign-mobile/dist /data/opensign-web
ADD ./opensign-message/dist /data/opensign-web
ADD ./opensign-manage/dist /data/opensign-web
ADD ./handwriting /data/opensign-web/handwriting


ADD ./nginx-config/nginx.conf /etc/nginx/nginx.conf
ADD ./nginx-config/default.template /etc/nginx/conf.d/default.template

ENTRYPOINT envsubst '$API_SERVER_NAME $DOMAIN_NAME'  < /etc/nginx/conf.d/default.template > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'
