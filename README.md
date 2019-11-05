# XML -> YAML converter

## Usage

Clone the repository
```
$ git clone https://github.com/loosebazooka/xml-yaml-converter.git
```

Run the converter
```
$ ./gradlew runConverter -Pwebapp.dir=<path to your webapp dir>

# or use the sample files

$ ./gradlew runConverter -Pwebapp.dir=src/test/resources/webapp

```
`<path to your webapp dir>` can be anywhere, it doesn't have to be a whole appengine app
but it must be structured like below because the underlying tooling expects that, it 
can contain any combinations of the xmls (it doesn't handle web.xml or appengine-web.xml),
and it will just convert what's present.
```
webapp/
└── WEB-INF
    ├── cron.xml
    ├── datastore-indexes.xml
    ├── dispatch.xml
    ├── dos.xml
    └── queue.xml
```

Congrats, your yamls are in
```
$ tree build/yaml-out

yaml-out/
├── cron.yaml
├── index.yaml
├── dispatch.yaml
├── dos.yaml
└── queue.yaml

```
