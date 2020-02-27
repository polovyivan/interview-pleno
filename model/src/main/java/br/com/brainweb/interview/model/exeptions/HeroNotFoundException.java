package br.com.brainweb.interview.model.exeptions;

import java.util.UUID;

public class HeroNotFoundException extends RuntimeException {

    public HeroNotFoundException(String uuid) {
        super(String.format("Hero with the name %s does not exists ", uuid));
    }

    public HeroNotFoundException(UUID uuid) {
        super(String.format("Hero with id %s does not exists ", uuid));
    }
}
