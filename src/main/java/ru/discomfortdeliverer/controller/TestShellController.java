package ru.discomfortdeliverer.controller;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class TestShellController {
    @ShellMethod(key = "hello", value = "Say hello")
    public String hello() {
        return "Hello, Spring Shell!";
    }
}
