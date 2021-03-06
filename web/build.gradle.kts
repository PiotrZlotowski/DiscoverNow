import com.moowork.gradle.node.npm.NpmTask

plugins {
  id("java")
  id("com.moowork.node") version "1.3.1"
}

node {
  npmVersion = "6.9.0"
  download = true
}

tasks {
  jar {
    from("dist/web").into("public")
    dependsOn("npm_run_build")
  }
  test {
    dependsOn("npm_test")
  }

  "npm_test"(NpmTask::class) {
    setNpmCommand("run", "test-headless")
  }
}




