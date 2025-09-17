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

3. npm create vite@latest reactivite
    npm create vite@latest reactivite --template react

cd reactivite
npm install -g generate-react-cli
npm install or yarn install - Install dependencies with 
npm run dev or yarn run dev - start server
npm run build or yarn run build  - build prod bundle
npx kill-port 3000

4. add new component: npx generate-react-cli component MyListBox

Running in Production Mode Locally:
    Modify the scripts section in package.json to include "serve": "vite preview".
    Run npm run build followed by npm run serve or npm run preview.

Configuration:
    Vite configuration is handled in the vite.config.js or vite.config.ts file.
    Plugins, such as the official @vitejs/plugin-react, can be added to extend Vite's functionality.


Current setup stack:

javaugi@DavidLee-PC MINGW64 /e/springtutorial/cimathformulas/src/main/webapp (master)
$ npm create vite@latest reactivite --template react

> npx
> create-vite reactivite react

|
o  Select a framework:
|  React
|
o  Select a variant:
|  React Router v7 ↗
Need to install the following packages:
create-react-router@7.5.3
Ok to proceed? (y) y

npm warn deprecated inflight@1.0.6: This module is not supported, and leaks memory. Do not use it. Check out 
    lru-cache if you want a good and tested way to coalesce async requests by a key value, which is much more comprehensive and powerful.
npm warn deprecated glob@7.2.3: Glob versions prior to v9 are no longer supported

> npx
> create-react-router reactivite

         create-react-router v7.5.3
      ◼  Directory: Using reactivite as project directory

      ◼  Using default template See https://github.com/remix-run/react-router-templates for more
      ✔  Template copied

   git   Initialize a new git repository?
         No

  deps   Install dependencies with npm?
         Yes

      ✔  Dependencies installed

  done   That's it!

         Enter your project directory using cd .\reactivite
         Check out README.md for development and deploy instructions.
         Join the community at https://rmx.as/discord

React Router v7, released in late 2024, represents a significant evolution of the popular routing library for React applications. It incorporates 
many features previously found in Remix, aiming to provide a more comprehensive and performant routing solution. React Router v7 introduces 
a "framework mode" alongside the traditional "library mode," allowing developers to build full-stack applications with server-side rendering 
capabilities, similar to frameworks like Next.js.
Key features and improvements in React Router v7 include:
Framework Mode:
Enables server-side rendering (SSR), data loading with loaders and actions, and form handling, expanding React Router's capabilities beyond client-side routing.
Performance Optimizations:
v7 boasts a smaller bundle size, a more efficient route matching algorithm, better code splitting support, and reduced memory usage compared to v6.
Data Loading and Actions:
Inspired by Remix, v7 introduces loaders for fetching data before rendering routes and actions for handling form submissions and data mutations.
Enhanced Routing:
v7 offers improved routing capabilities, including nested routes and layout routes, for building more complex and organized application structures.
Improved Type Safety:
New type generation provides better type checking for route parameters, loader data, and actions, enhancing code maintainability.
Vite Plugin:
A dedicated Vite plugin facilitates the adoption of framework features and provides a streamlined development experience.
Non-breaking Upgrade:
Upgrading from v6 to v7 is designed to be a non-breaking process, ensuring a smooth transition for existing projects.
React Router v7 offers developers the flexibility to choose between "library mode" for traditional client-side routing and "framework mode"
 for full-stack applications with SSR. For many applications, v7 alone may suffice, while Remix remains a suitable choice for projects requiring 
advanced integrations or those already using it.

In Vite, TypeScript SWC refers to the integration of the Speedy Web Compiler (SWC) as a replacement for the default TypeScript compiler, 
tsc, or Babel, for transforming TypeScript and JavaScript code. SWC, written in Rust, is designed for high performance and offers 
significantly faster compilation speeds, making it particularly beneficial for large projects and improving the development experience.
When creating a Vite project, opting for "TypeScript + SWC" instead of "TypeScript" configures Vite to use SWC for transpilation. 
This choice can lead to faster build times and potentially smaller output sizes. SWC achieves its speed through its Rust-based 
implementation and efficient handling of code transformations. While generally faster, SWC may have limitations in supporting 
all features of tsc and Babel, though it is actively developed and widely used in projects like Next.js.
Using SWC in Vite can enhance the development workflow by providing rapid feedback through Hot Module Replacement (HMR) and quicker 
build processes. It is especially advantageous in scenarios where build times are a bottleneck, allowing developers to iterate more efficiently.

React development using Vite involves setting up and building React applications with Vite, a fast build tool that offers a streamlined development experience.
Project Setup:
Use the command npm create vite@latest or yarn create vite to create a new Vite project.
Select React as the framework and choose either JavaScript or TypeScript.
Navigate into the project directory using cd your-project-name.
Install dependencies with npm install or yarn install.
Development Server:
Start the development server using npm run dev or yarn run dev.
Vite's dev server leverages native ES modules, enabling fast startup and Hot Module Replacement (HMR) for instant updates in the browser.
Building for Production:
Build an optimized production bundle using npm run build or yarn run build.
Vite uses Rollup under the hood for production builds, ensuring efficient code splitting and optimization.
Configuration:
Vite configuration is handled in the vite.config.js or vite.config.ts file.
Plugins, such as the official @vitejs/plugin-react, can be added to extend Vite's functionality.
Key Features:
Fast Startup: Vite's on-demand compilation drastically reduces startup time.
Hot Module Replacement (HMR): Changes in code are reflected instantly in the browser without full page reloads.
Optimized Builds: Production builds are highly optimized for performance and smaller file sizes.
Plugin Ecosystem: Vite's plugin system allows for customization and integration with other tools.
Support for TypeScript and JSX: Vite has built-in support for TypeScript and JSX, with esbuild or SWC for fast transpilation.
Deployment:
After building, the dist folder contains the production-ready assets.
These assets can be deployed to various hosting platforms, such as GitHub Pages, Netlify, or Vercel.
Running in Production Mode Locally:
Modify the scripts section in package.json to include "serve": "vite preview".
Run npm run build followed by npm run serve or npm run preview


Here are examples of React development using Vite:
Setting up a new project:
Run the following command in your terminal:
Code

        npm create vite@latest my-react-app --template react
Replace my-react-app with your desired project name. Navigate to the project directory.
Code

        cd my-react-app
Install dependencies.
Code

        npm install
Running the development server:
Start the development server with:
Code

        npm run dev
The application will be served at http://localhost:5173/ or a similar address, which will be shown in the terminal.
Project structure:
node_modules: Contains project dependencies.
public: Directory for static assets.
src: Contains the main application code.
App.jsx or App.tsx: Main application component.
main.jsx or main.tsx: Entry point of the application.
assets: For images and other assets.
components: For React components.
Example component:
Create a new file in the src/components directory, for example, Counter.jsx:
Code

        // src/components/Counter.jsx
        import React, { useState } from 'react';

        function Counter() {
          const [count, setCount] = useState(0);

          return (
            <div>
              <p>Count: {count}</p>
              <button onClick={() => setCount(count + 1)}>Increment</button>
            </div>
          );
        }

        export default Counter;
Import and use the component in App.jsx:
Code

        // src/App.jsx
        import React from 'react';
        import Counter from './components/Counter';
        import './App.css';

        function App() {
          return (
            <div className="App">
              <h1>React with Vite</h1>
              <Counter />
            </div>
          );
        }

        export default App;
Building for production:
Run the build command:
Code

        npm run build
The production-ready files will be in the dist directory.