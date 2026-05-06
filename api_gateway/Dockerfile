FROM node:18

WORKDIR /app

COPY api_gateway/package*.json ./

RUN npm install

COPY api_gateway/. .

EXPOSE 8090

CMD ["node", "index.js"]
