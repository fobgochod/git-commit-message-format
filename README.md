# Git Commit Helper

![Build](https://github.com/fobgochod/git-commit-helper/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

<!-- Plugin description -->

This plugin allows to create a commit message with the following template:

```
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

<!-- Plugin description end -->

## Field description

- **Breaking Changes** are any changes that might require action from our integrators. We divide these changes into two
  categories:
    - **Breaking**: Changes that will break existing queries to the GraphQL API. For example, removing a field would be
      a
      breaking change.
    - **Dangerous**: Changes that won't break existing queries but could affect the runtime behavior of clients. Adding
      an
      enum value is an example of a dangerous change.

- **Closed Issues**: You can close an issue when bugs are fixed, feedback is acted on, or to show that work is not
  planned.

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "
  Git Commit Helper"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/fobgochod/git-commit-helper/releases/latest) and install it manually
  using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
