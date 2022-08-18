# scoot

## Installation

The module is under `src/scoot`. You can copy this subtree into your
project and then add it to the `package.path` in your Scopes
`_project.sc` file.

### With Spack

This module is available as the `scoot` package in the
[snailpacks](https://github.com/salotz/snailpacks) repository. This will pull in the necessary dependencies
including Scopes.

```sh
  spack install scoot
```

See the [snailpacks](https://github.com/salotz/snailpacks) documentation for more best practices of installing.

## Development Environment

We use [Spack](https://spack.io/) to install dependencies. First install Spack.

Then you'll need our custom repo of build recipes:

```sh
  mkdir -p `/.spack/repos
  git clone git@github.com:salotz/snailpacks.git `/.spack/repos/snailpacks
  spack repo add `/resources/spack-repos/snailpacks
```

Then you need to create an environment in this folder that will
contain the headers and libraries etc., this will create this and
install the packages:

```sh
  make init
```

Then you can activate the environment to get started:

```sh
  spacktivate .
```

Run some commands:

```sh
# run the sanity entrypoint
make sanity

# run the tests
make test
```

To exit the environment (i.e. unset the env variables):

```sh
  despacktivate
```

