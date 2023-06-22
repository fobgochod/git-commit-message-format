# [![Plugin Logo](doc/flogo.svg)][plugin:logo] Git Commit Message Format

[![Build Status][badge:build]][gh:workflow-build]
[![License][badge:license]][gh:license]
[![GitHub releases][badge:release]][gh:releases]
[![Plugin Version][badge:version]][plugin:versions]
[![Plugin Downloads][badge:downloads]][plugin:homepage]



<!-- Plugin description -->

## Usage

- Click birdie or press **Alt + Q** open formatter dialog
- Click outside or press **Alt + Enter** to be completed

## Commit Message Format

Each commit message consists of a **header**, a **body** and a **footer**. The header has a special
format that includes a **type**, a **scope** and a **subject**:

```
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

The **header** is mandatory and the **scope** of the header is optional.

Any line of the commit message cannot be longer than 100 characters! This allows the message to be easier
to read on GitHub as well as in various git tools.

### Revert

If the commit reverts a previous commit, it should begin with `revert: `, followed by the header
of the reverted commit.
In the body it should say: `This reverts commit <hash>.`, where the hash is the SHA of the commit
being reverted.

### Type

Must be one of the following:

* **feat**: A new feature
* **fix**: A bug fix
* **docs**: Documentation only changes
* **style**: Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)
* **refactor**: A code change that neither fixes a bug nor adds a feature
* **perf**: A code change that improves performance
* **test**: Adding missing or correcting existing tests
* **chore**: Changes to the build process or auxiliary tools and libraries such as documentation
  generation

### Scope

The scope could be anything specifying place of the commit change. For example `$location`,
`$browser`, `$compile`, `$rootScope`, `ngHref`, `ngClick`, `ngView`, etc...

You can use `*` when the change affects more than a single scope.

### Subject

The subject contains succinct description of the change:

* use the imperative, present tense: "change" not "changed" nor "changes"
* don't capitalize first letter
* no dot (.) at the end

### Body

Just as in the **subject**, use the imperative, present tense: "change" not "changed" nor "changes".
The body should include the motivation for the change and contrast this with previous behavior.

### Footer

The footer should contain any information about **Breaking Changes** and is also the place to
[reference GitHub issues that this commit closes](https://help.github.com/articles/closing-issues-via-commit-messages).

**Breaking Changes** should start with the word `BREAKING CHANGE:` with a space or two newlines.
The rest of the commit message is then used for this.

A detailed explanation can be found in
this [document](https://docs.google.com/document/d/1QrDFcIiPjSLDn3EL15IJygNPiHORgU1_OOAqWjiDU5Y/edit#).

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Git Commit Message
  Format"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:
  Download the [latest release][gh:releases] or [JetBrains Plugin Marketplace][plugin:versions] and install it
  manually using <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from
  disk...</kbd>

## Usage

![commit-step1](doc/commit-template-1.png)

![commit-step2](doc/commit-template-2.png)

![commit-step3](doc/commit-template-3.png)

## Reference

- [Git Commit Template][plugin:commit-template-idea-plugin]
- [Git Commit Message Helper][plugin:git-commit-message-helper]
- [The commitizen command line utility][plugin:cz-cli]
- [AngularJS's commit message convention][docs:commit-message-convention]
- [Commit message 和 Change log 编写指南][docs:commit-message-change-log]

## License

The Git Commit Message Format is released under version 2.0 of
the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).


[//]: # (@formatter:off)
[badge:build]: https://github.com/fobgochod/git-commit-message-format/workflows/Build/badge.svg
[badge:license]: https://img.shields.io/github/license/fobgochod/git-commit-message-format.svg?color=4D7A97&logo=apache
[badge:release]: https://img.shields.io/github/release/fobgochod/git-commit-message-format.svg?colorB=0097A7
[badge:version]: https://img.shields.io/jetbrains/plugin/v/20935.svg
[badge:downloads]: https://img.shields.io/jetbrains/plugin/d/20935.svg

[gh:workflow-build]: https://github.com/fobgochod/git-commit-message-format/actions?query=workflow%3ABuild
[gh:license]: https://github.com/fobgochod/git-commit-message-format/blob/main/LICENSE
[gh:releases]: https://github.com/fobgochod/git-commit-message-format/releases

[plugin:logo]: https://github.com/cncf/landscape/blob/master/hosted_logos/flogo.svg
[plugin:homepage]: https://plugins.jetbrains.com/plugin/20935
[plugin:versions]: https://plugins.jetbrains.com/plugin/20935/versions
[plugin:cz-cli]: https://github.com/commitizen/cz-cli
[plugin:git-commit-message-helper]: https://github.com/AutismSuperman/git-commit-message-helper
[plugin:commit-template-idea-plugin]: https://github.com/MobileTribe/commit-template-idea-plugin

[docs:commit-message-change-log]: http://www.ruanyifeng.com/blog/2016/01/commit_message_change_log.html
[docs:commit-message-convention]: https://github.com/angular/angular.js/blob/master/DEVELOPERS.md#-git-commit-guidelines
