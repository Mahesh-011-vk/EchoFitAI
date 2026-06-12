# Contributing to EchoFit 🤝

We love your input! We want to make contributing to EchoFit as easy and transparent as possible, whether it's:

- Reporting a bug
- Discussing the current state of the code
- Submitting a fix
- Proposing new features

## Branching Strategy
We follow a standard Git Flow branching model:
- `main` branch holds the stable, production-ready release code.
- Create all feature branches off `main` using the format `feature/your-feature-name`.
- Create bugfix branches using the format `bugfix/your-bugfix-name`.

## Pull Request Process
1. Fork the repository and create your branch from `main`.
2. Implement your changes, ensuring no code styles are broken.
3. Test your changes locally:
   - For Android: Ensure `./gradlew assembleDebug` compiles successfully.
   - For Landing Page: Ensure `npm run build` succeeds under the `landing-page` directory.
4. Update the `README.md` if your changes alter user configuration or flow.
5. Submit a Pull Request targeting the `main` branch. Fill out the PR template completely.
6. The automated CI workflows must pass before the PR can be reviewed and merged.

## Code Style & Guidelines
- **Android (Kotlin)**: Follow official Kotlin Coding Conventions. Keep Composable functions concise and modular.
- **Web (React/TypeScript)**: Keep CSS files cleaned up and make sure state variables are type-safe. Avoid introducing unused imports or lint errors.
