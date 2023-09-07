package net.defekt.minecraft.starbox.data;

import com.google.gson.Gson;

import java.util.Arrays;

public class ChatMessage {
    private final String text;
    private final String translate;
    private final String color;
    private final ChatMessage[] extra;
    private final ChatMessage[] with;

    public static class Builder {

        private String text;
        private String translate;
        private String color;
        private ChatMessage[] extra;
        private ChatMessage[] with;

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setTranslate(String translate) {
            this.translate = translate;
            return this;
        }

        public Builder setColor(String color) {
            this.color = color;
            return this;
        }

        public Builder setExtra(ChatMessage[] extra) {
            this.extra = extra;
            return this;
        }

        public Builder setWith(ChatMessage[] with) {
            this.with = with;
            return this;
        }

        public Builder addExtra(ChatMessage component) {
            if (extra == null) extra = new ChatMessage[0];
            extra = Arrays.copyOf(extra, extra.length + 1);
            extra[extra.length - 1] = component;
            return this;
        }

        public Builder addWith(ChatMessage component) {
            if (with == null) with = new ChatMessage[0];
            with = Arrays.copyOf(with, with.length + 1);
            with[with.length - 1] = component;
            return this;
        }

        public Builder translateColors() {
            this.text = this.text.replace("&", "§");
            return this;
        }

        public ChatMessage build() {
            return new ChatMessage(text, translate, color, extra, with);
        }
    }

    public static ChatMessage fromString(String text) {
        return new Builder().setText(text).translateColors().build();
    }

    private ChatMessage(String text, String translate, String color, ChatMessage[] extra, ChatMessage[] with) {
        this.text = text;
        this.translate = translate;
        this.color = color;
        this.extra = extra;
        this.with = with;
    }


    public String getText() {
        return text;
    }

    public String getTranslate() {
        return translate;
    }

    public String getColor() {
        return color;
    }

    public ChatMessage[] getExtra() {
        return extra;
    }

    public ChatMessage[] getWith() {
        return with;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
