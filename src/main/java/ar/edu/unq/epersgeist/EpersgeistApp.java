package ar.edu.unq.epersgeist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories
public class EpersgeistApp {

    public static void main(String[] args) {
        epersASCII();
        SpringApplication.run(EpersgeistApp.class, args);
    }

    private static void epersASCII() {
        System.out.println();
        System.out.println("__/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\__/\\\\\\\\\\\\\\\\\\\\\\\\\\____/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\____/\\\\\\\\\\\\\\\\\\_________/\\\\\\\\\\\\\\\\\\\\\\___        ");
        System.out.println(" _\\/\\\\\\/////////////__\\/\\\\\\/////////\\\\\\_\\/\\\\\\//////////___/\\\\\\///////\\\\\\_____/\\\\\\/////////\\\\\\       ");
        System.out.println("  _\\/\\\\\\_____________\\/\\\\\\_______\\/\\\\\\_\\/\\\\\\_____________\\/\\\\\\_____\\/\\\\\\____\\//\\\\\\______\\///__      ");
        System.out.println("   _\\/\\\\\\\\\\\\\\\\\\\\\\_____\\/\\\\\\\\\\\\\\\\\\\\\\\\\\/__\\/\\\\\\\\\\\\\\\\\\\\\\_____\\/\\\\\\\\\\\\\\\\\\\\\\/______\\////\\\\\\_________     ");
        System.out.println("    _\\/\\\\\\///////______\\/\\\\\\/////////____\\/\\\\\\///////______\\/\\\\\\//////\\\\\\_________\\////\\\\\\______    ");
        System.out.println("     _\\/\\\\\\_____________\\/\\\\\\_____________\\/\\\\\\_____________\\/\\\\\\____\\//\\\\\\___________\\////\\\\\\___   ");
        System.out.println("      _\\/\\\\\\_____________\\/\\\\\\_____________\\/\\\\\\_____________\\/\\\\\\_____\\//\\\\\\___/\\\\\\______\\//\\\\\\__  ");
        System.out.println("       _\\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_\\/\\\\\\_____________\\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_\\/\\\\\\______\\//\\\\\\_\\///\\\\\\\\\\\\\\\\\\/_____");
        System.out.println("        _\\///////////////__\\///______________\\///////////////__\\///________\\///____\\///////////_____");
    }

}