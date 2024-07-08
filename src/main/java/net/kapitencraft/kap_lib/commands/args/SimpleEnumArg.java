package net.kapitencraft.kap_lib.commands.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class SimpleEnumArg<T extends Enum<T>> implements ArgumentType<T> {
    private final Function<T, String> mapper;
    private final T[] elements;

    protected SimpleEnumArg(Function<T, String> mapper, T[] elements) {
        this.mapper = mapper;
        this.elements = elements;
    }

    private T getElement(String in) {
        for (T element : elements) {
            if (Objects.equals(in, mapper.apply(element))) return element;
        }
        return null;
    }

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        return getElement(reader.readString());
    }

    @Override
    public Collection<String> getExamples() {
        return getRegNames().toList();
    }

    private Stream<String> getRegNames() {
        return Arrays.stream(elements).map(mapper);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(getRegNames(), builder);
    }
}
