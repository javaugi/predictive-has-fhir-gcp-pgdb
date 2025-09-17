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
4. install react native 
    npx react-native init myreactnative
    or 
    npx react-native@0.79.2 init myreactnative --version 0.79.2
5. install react js
    npm create vite@latest my-react-app -- --template react
    - to run
    npm run dev:  http://localhost:5173/

Next.js is primarily used for web application development, offering features like server-side rendering and static site generation. 
React Native, on the other hand, is a framework for building native mobile applications. While they serve different purposes, they 
    can be used together in the same project to share code and create a unified development experience for web and mobile platforms.
Next.js can be used as a backend for a React Native app, providing API endpoints for data fetching and other server-side logic. 
    This allows developers to use the same language (JavaScript/TypeScript) for both the frontend (React Native) and the backend (Next.js).
    Additionally, with libraries like react-native-web, it's possible to share UI components and logic between a Next.js web app and a
    React Native mobile app. This approach enables cross-platform development, reducing development time and increasing maintainability.

6. npx create-next-app@latest myreactnext - to create react js project using Next.js framwork
Ok to proceed? (y) y

√ Would you like to use TypeScript? ... No / Yes
√ Would you like to use ESLint? ... No / Yes
√ Would you like to use Tailwind CSS? ... No / Yes
√ Would you like your code inside a `src/` directory? ... No / Yes
√ Would you like to use App Router? (recommended) ... No / Yes
√ Would you like to use Turbopack for `next dev`? ... No / Yes
√ Would you like to customize the import alias (`@/*` by default)? ... No / Yes
Creating a new Next.js app in E:\springtutorial\cimathformulas\src\main\webapp\myreactnext.
Using npm.
Initializing project with template: app-tw
Installing dependencies:
- react
- react-dom
- next
Installing devDependencies:
- typescript
- @types/node
- @types/react
- @types/react-dom
- @tailwindcss/postcss
- tailwindcss

added 48 packages, and audited 49 packages in 33s
9 packages are looking for funding
  run `npm fund` for details
found 0 vulnerabilities
Success! Created myreactnext at E:\springtutorial\cimathformulas\src\main\webapp\myreactnext

7. cd to any app created and find all elated packages and their versions
npm fund

8. https://reactnative.dev/docs/getting-started  
https://www.tutorialspoint.com/react_native/index.htm


10.  npx create-expo-app myreactnativeexpo

To run your project, navigate to the directory and run one of the following npm commands.

- cd myreactnative
- npm run android
- npm run ios # you need to use macOS to build the iOS project - use the Expo app if you need to do iOS development without a Mac
- npm run web
