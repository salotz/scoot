
init: .spack-env
.PHONY: init

.spack-env: spack.yaml
	spack env create -d .
	spack -e . install

sanity:
	scopes -e -m scoot.sanity
.PHONY: sanity

test:
	scopes -e ./tests/*.sc
.PHONY: test

clean:
	rm -rf .spack-env
.PHONY: clean
