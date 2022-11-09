def call() {
    def image =  new DockerPublishCommand();
    image.withTag("TESTST");
    image.build();
}

