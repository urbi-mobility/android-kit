# Change Log

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]

## [1.0.0] - 2026-01-20

### Added

- Feat: Configuration for public repository
- Feat: Corruption handler and logging support for data stores
- Feat: Support for Google Tink in addition to previous Cipher method
- Feat: Documentation generation tools
- Feat: Preference Data Storage
- Feat: Interface for encryption setup

### Changed

- Refactor: Logic of data store to be compatible with both Tink and Cipher
- Refactor: Data store setup builder pattern
- Style: Updated code formatting and imports
- Docs: Updated read me and change log

### Fixed

- Fix: Updated log text for proto data store
- Perf: Added exception handling for serializers read function
- Perf: Improved application context handling
