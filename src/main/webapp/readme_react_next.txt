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




Yes, while you can technically build a React app from scratch, using a framework like Create React App or Vite is highly recommended, especially for new projects. These frameworks provide pre-configured environments, streamline the development process, and offer various benefits like faster development servers and optimized builds. 
Here's a more detailed explanation:
Why use a framework?
Simplified Setup:
Frameworks handle the complexities of setting up a React project, including bundling, transpiling, and more. 
Performance:
Frameworks often include tools like webpack or Vite, which can optimize your app for faster loading and improved performance. 
Community Support:
Popular frameworks have large communities and extensive documentation, making it easier to find solutions to problems and learn new techniques. 
Standard Practices:
Frameworks often enforce good coding practices and architectural patterns, helping to create maintainable and scalable applications. 
When to consider building from scratch:
Learning the Basics:
Building from scratch can be a valuable learning experience, allowing you to understand how React works under the hood. 
Highly Specialized Needs:
If your application has very specific requirements that are not well-served by existing frameworks, you might consider building your own framework. 
Testing and Learning:
Some developers might choose to start from scratch to experiment with different tools and approaches. 
Popular React Frameworks:
Create React App:
.
A widely used framework for creating single-page applications, but it's currently deprecated and no longer recommended for new projects. 
Vite:
.
A modern and fast build tool that can be used with React, offering a faster development experience. 
Next.js:
.
A framework for building full-fledged React applications with server-side rendering, static generation, and other advanced features. 
Remix:
.
A framework that focuses on the web's native capabilities, offering a different approach to React development. 
In summary: While you can build a React app without a framework, starting with one is generally recommended, especially for new projects. 
    Frameworks streamline development, offer performance advantages, and provide a robust foundation for building scalable applications. If you're 
    learning or have specific needs, you might consider building from scratch, but for most cases, a framework is the best approach. 


Choosing between Vite and Next.js for a React project depends on the project's specific requirements. Here's a comparison to help in the decision-making process:
Vite
Vite is a build tool that aims to provide a faster and leaner development experience for modern web projects. It excels in speed and simplicity, making it suitable for:
Small to medium-sized projects:
.
Vite is ideal for projects that don't require server-side rendering or complex routing.
Single-page applications (SPAs):
.
Its fast refresh and build times make it efficient for developing SPAs.
Component libraries:
.
Vite's simplicity and speed are beneficial for creating and testing UI components.
Rapid prototyping:
.
The quick setup and minimal configuration allow for fast iteration and experimentation.
Next.js
Next.js is a full-fledged React framework that offers a range of features for building robust web applications. It's well-suited for:
Large-scale applications:
.
Next.js provides the structure and tools needed for complex projects with many pages and components.
SEO-heavy websites:
.
Server-side rendering (SSR) and static site generation (SSG) capabilities improve SEO performance.
Dynamic content websites:
.
Features like incremental static regeneration (ISR) and API routes make it suitable for handling frequently updated content.
Full-stack applications:
.
Next.js allows for building both the front-end and back-end within the same project.
Applications requiring authentication and data fetching:
.
Next.js offers built-in solutions for these common requirements.
Key Differences
Feature
Vite
Next.js
Build Process
Fast, uses native ES modules
More complex, server-side rendering
Rendering
Client-side
Server-side, static site generation, client-side
Routing
Basic, relies on libraries like React Router
Built-in, advanced routing features
Data Fetching
Requires manual setup
Built-in methods for data fetching
SEO
Less optimized for SEO
Highly optimized for SEO
Complexity
Simpler, easier to learn
More complex, steeper learning curve
Project Size
Best for small to medium projects
Best for medium to large projects
Considerations
Project Size and Complexity:
If the project is small or doesn't require server-side rendering, Vite is a great choice for its speed and simplicity. For larger, more complex applications, Next.js offers the necessary structure and features.
SEO Requirements:
If SEO is a priority, Next.js is the better option due to its server-side rendering and static site generation capabilities.
Development Speed:
Vite's fast build times and hot module replacement can significantly speed up development, especially for front-end focused projects.
Learning Curve:
Vite is easier to learn and set up, while Next.js has a steeper learning curve due to its more extensive feature set.
Conclusion
The choice between Vite and Next.js depends on the specific needs of the project. Vite is ideal for smaller, client-side applications where speed and simplicity are key, while Next.js is better suited for larger, more complex applications that require server-side rendering, SEO optimization, and a full-featured framework.
