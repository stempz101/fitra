FROM node:20-alpine
WORKDIR /app
COPY public/ ./public
COPY src/ ./src
COPY package*.json ./
RUN npm install --force
EXPOSE 3000:3000
ENTRYPOINT ["npm", "start"]
