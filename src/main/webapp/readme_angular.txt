1. download and install node.js or use nvm
$ nvm use 16
Now using node v16.9.1 (npm v7.21.1)
$ node -v
v16.9.1
$ nvm use 14
Now using node v14.18.0 (npm v6.14.15)
$ node -v
v14.18.0
$ nvm install 12
Now using node v12.22.6 (npm v6.14.5)
$ node -v
v12.22.6

2. install npm: npm install -g npm
node -v
npm -v

3. install angular: npm install -g @angular/cli
    ng new myangularapp
    npm start 
    or
    ng serve –open
    http://localhost:4200/

4. cd to any app created and find all elated packages and their versions
npm fund
