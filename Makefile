
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
	scopes -e ./tests/**/*.sc
.PHONY: test

scopes-pkg.json: scopes-pkg.slon
	sln-to-json $^ > $@

validate: validate-pkg
.PHONY: validate

validate-pkg: scopes-pkg.json
	check-jsonschema \
		--schemafile https://raw.githubusercontent.com/salotz/scopes-packaging/master/schemas/json-schemas/scopes-pkg.schema.json \
		$^
.PHONY: validate-pkg

clean:
	rm -rf .spack-env
.PHONY: clean
