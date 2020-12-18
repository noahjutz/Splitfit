# Splitfit contribution guidelines

Thanks for contributing to Splitfit!

## Issue reporting/feature requests

* **Already reported**? Browse the [existing issues](https://github.com/noahjutz/Splitfit/issues) to make sure your issue/feature hasn't been reported/requested.
* **Already fixed**? Check whether your issue/feature is already fixed/implemented.
* **Still relevant**? Check if the issue still exists in the latest release/beta version.
* **Can you fix it**? If you are an Android/Java developer, you are always welcome to fix an issue or implement a feature yourself. PRs welcome! See [Code contribution](#code-contribution) for more info.
* **Is it in English**? Issues in other languages will be ignored unless someone translates them.
* **Is it one issue**? Multiple issues require multiple reports, that can be linked to track their statuses.
* **The template**: Fill it out, everyone wins. Your issue has a chance of getting fixed.

## Code contribution

* If you want to help out with an existing bug report or feature request, leave a comment on that issue saying you want to try your hand at it.
* If there is no existing issue for what you want to work on, open a new one describing your changes. This gives the team and the community a chance to give feedback before you spend time on something that is already in development, should be done differently, or should be avoided completely.
* Stick to Splitfit's style conventions of [Ktlint](https://ktlint.github.io/). Run it using `./gradlew ktlintCheck`.
* Do not bring non-free software (e.g. binary blobs) into the project. Make sure you do not introduce Google
  libraries.
* Stick to [F-Droid contribution guidelines](https://f-droid.org/wiki/page/Inclusion_Policy).
* Make changes on a separate branch with a meaningful name, not on the _master_ branch branch. This is commonly known as *feature branch workflow*. You may then send your changes as a pull request (PR) on GitHub.
* Please test (compile and run) your code before submitting changes! Ideally, provide test feedback in the PR description. Untested code will **not** be merged!
* Make sure your PR is up-to-date with the rest of the code.
* Please show intention to maintain your features and code after you contribute a PR. Unmaintained code is a hassle for core developers. If you do not intend to maintain features you plan to contribute, please rethink your submission, or clearly state that in the PR description.
* Respond if someone requests changes or otherwise raises issues about your PRs.
* Send PRs that only cover one specific issue/solution/bug. Do not send PRs that are huge and consist of multiple independent solutions.
* Try to figure out yourself why builds on our CI fail.

## Communication

* Contact me at noahjutz@tutanota.de
* Post suggestions, changes, ideas etc. on GitHub

---
This is a modified version of [NewPipe's CONTRIBUTING.md](https://github.com/TeamNewPipe/NewPipe/blob/3ad14e4adf04d516a3e29d03268ba214c79671fa/.github/CONTRIBUTING.md)
