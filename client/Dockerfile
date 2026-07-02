FROM node:20-alpine

WORKDIR /app

COPY package.json package-lock.json ./

RUN npm install

COPY . .

EXPOSE 1410

CMD ["npm", "run", "dev", "--", "--host"]