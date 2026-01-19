# Urbi Android Kit

A Kotlin Android library collection ('Urbi-Kit') providing reusable, production-ready components and utilities used across Urbi mobile apps. The repository is organized as a monorepo with small, focused modules that are easy to adopt in apps or other libraries.

## Key goals:

- Provide simple, well-documented building blocks for Android apps
- Use modern Kotlin, Kotlinx Serialization, Coroutines, Jetpack libraries
- Offer optional secure defaults (e.g., encrypted storage) for sensitive data

---

## 🚀 Getting Started

### Clone the Repository

When cloning this repository, make sure to initialize the submodules:

```bash
# Clone with submodules
git clone --recurse-submodules https://github.com/urbi-mobility/android-kit.git

# Or if you already cloned it, initialize submodules separately
git submodule update --init --recursive
```

### Build the Project

After cloning with submodules:

```bash
./gradlew clean build
```

---

## ⚙️ Configuration

Please check each module's folder to see how to set it up.

## 📚 Documentation

To generate the documentation for the modules (e.g., `data_store`), run the following command to generate Markdown files:

```bash
./gradlew :data_store:dokkaGfm
```

The generated documentation will be available in `data_store/build/dokka/gfm`.

## 📁 Modules

This is part of the larger `Urbi-Kit` monorepo:

- ✅ [`Urbi-Kit-SecureDataStore`](https://github.com/urbi-mobility/android-kit/tree/main/data_store) – An easy way of securly storing user data using jetpack data store.
- ⏳ More coming soon...

---

## 🧑‍💻👩‍💻 Contributing

Contributions are welcome. Please:

- Open an issue to discuss larger changes
- Create PRs with small, focused changes and tests/examples when applicable
- Follow standard Kotlin/Android style, add documentation and examples

## ✉️ Contact

For questions about usage or integration, open an issue in this repository.
