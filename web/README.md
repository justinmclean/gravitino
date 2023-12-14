<!--
  Copyright 2023 Datastrato Pvt Ltd.
  This software is licensed under the Apache License version 2.
-->

# Gravitino web interface

> **⚠️ Important**
>
> Before running commands, make sure that you are in the front-end directory `gravitino/web`.

---

## Getting started

### Preparation, framework and dependencies

- [Node.js](https://nodejs.org)(v20.x+) & [npm](https://www.npmjs.com/) / [yarn](https://yarnpkg.com/)
- [React](https://react.dev/)
- [Next.js](https://nextjs.org)
- [MUI](https://mui.com/)
- [tailwindcss](https://tailwindcss.com/)
- [`react-redux`](https://react-redux.js.org/)

> **Tip**
>
> It's recommended to use the Yarn package manager.
>
> **Requirements**
>
> Please make sure you use the node’s LTS version
> Before installing the **node modules**, make sure files starting with a dot, such as **(.eslintrc, .env etc..)** exist.

## Installation

### Development environment

- Run the following commands in console:

```bash
# install dependencies
yarn install
```

- After installing the modules run your project with following command:

```bash
# start development server
yarn server
```

- Visit <http://localhost:3000> in your browser to view the Gravitino web interface. You can start editing the pages, for example `pages/index.js` and the interface auto-updates as you make changes.

### Development scripts

```bash
yarn lint
# Runs ESLint to inspect the code. If any errors are displayed, please make necessary changes.
```

```bash
yarn prettier:check
# run this command to run Prettier and check your code style. You can manually fix any issues, or use yarn format to automatically correct the code using the Prettier CLI.
```

```bash
yarn format
# This command helps you automatically format the code.

```

## Self-hosting deployment

### Node.js server

Deploy Next.js to any hosting provider supporting Node.js by ensuring that your `package.json` contains the `build` and `start` scripts.

```json
{
  "scripts": {
    "server": "next dev",
    "build": "next build",
    "start": "next start"
  }
}
```

`next build` builds the production app in the `.next` folder. After building, `next start` starts a Node.js server that supports hybrid pages, serving both statically generated and server-side rendered pages.

```bash
# build production files
yarn build

# start the nodejs server
yarn start
```

### Static HTML export

Use the `next export` command to export your app to static HTML, enabling it to run standalone without requiring a Node.js server.

The `next export` command generates a `dist` directory, which any static hosting service can serve.

```bash
yarn dist
# then copy the files within the 'dist' directory to the root directory of the static server
```

## Docker

Make sure you have installed a recent version of [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/install/#scenario-two-install-the-compose-plugin). [Docker Desktop](https://www.docker.com/products/docker-desktop/) already includes Docker Engine, Docker CLI and Docker Compose.

> **⚠️ Important**
>
> Run all the following commands in a `macOS` environment. If you are using a different system, you may encounter errors.
> Please modify the commands according to the system prompts. For example, if you are using `Windows`, replace `${PWD}` with `%cd%`, etc.

Only use Docker to build the static `HTML\CSS\JS` files directory.

Run the following command in the console:

```bash
# ensure you are in the `web` directory
docker run -it --rm --name gravitino-web-docker -v ${PWD}:/web -w /web node:20-slim /bin/bash -c "yarn install && yarn dist"
docker run -it -p 3000:3000 -v ${PWD}:/web -w /web --name gravitino-web node:20-slim /bin/bash
docker run -p 3000:3000 -v ${PWD}:/web --name gravitino-web node:20-slim /bin/bash -c "yarn install && yarn dist"
```

Run the command `yarn install` to install the dependencies specified in the `package.json` file, followed by `yarn export` to generate a static version of the app. The process stores the resulting files in the dist directory within the container, linking it to the dist directory in the current directory of the host machine. The exported files are accessible on the host machine after executing this command.

If you also want to start a server to view the demo, please use the following command:

```bash
docker run -it --rm --name gravitino-web-docker -v ${PWD}:/web -p 3000:3000 -w /web node:20-slim /bin/bash -c "yarn install && yarn server"
```

You can access the Gravitino Web UI by typing <http://localhost:3000> in your browser.
