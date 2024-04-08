FROM node:18-alpine
WORKDIR /app
# COPY . .
# RUN npm install
#the following supports dependency caching
COPY package.json package-lock.json ./ 
RUN npm install
COPY . .
EXPOSE 5173
CMD ["npm", "run", "dev"]