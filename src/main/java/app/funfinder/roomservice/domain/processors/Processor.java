package app.funfinder.roomservice.domain.processors;

public interface Processor<I, O> {

    O process(I input);
}
