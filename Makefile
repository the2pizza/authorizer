.DEFAULT_GOAL := all

VERSION=$(shell cat version.txt)
APP="target/uberjar/authorizer-$(VERSION)-standalone.jar"

build:
	lein uberjar

.PHONY: test
test:
	lein test

.PHONY: e2e
e2e:
	./bin/e2e-tests-run

.PHONY: install
install:
	bin/install.sh

.PHONY:all
all: build test e2e
