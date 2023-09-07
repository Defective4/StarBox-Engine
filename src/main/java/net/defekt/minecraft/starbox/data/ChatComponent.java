package net.defekt.minecraft.starbox.data;

import com.google.gson.Gson;

import java.util.Arrays;

public class ChatComponent {
    private final String text;
    private final String translate;
    private final String color;
    private final ChatComponent[] extra;
    private final ChatComponent[] with;

    public static class Builder {

        private String text;
        private String translate;
        private String color;
        private ChatComponent[] extra;
        private ChatComponent[] with;

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

        public Builder setExtra(ChatComponent[] extra) {
            this.extra = extra;
            return this;
        }

        public Builder setWith(ChatComponent[] with) {
            this.with = with;
            return this;
        }

        public Builder addExtra(ChatComponent component) {
            if (extra == null) extra = new ChatComponent[0];
            extra = Arrays.copyOf(extra, extra.length + 1);
            extra[extra.length - 1] = component;
            return this;
        }

        public Builder addWith(ChatComponent component) {
            if (with == null) with = new ChatComponent[0];
            with = Arrays.copyOf(with, with.length + 1);
            with[with.length - 1] = component;
            return this;
        }

        public Builder translateColors() {
            this.text = this.text.replace("&", "§");
            return this;
        }

        public ChatComponent build() {
            return new ChatComponent(text, translate, color, extra, with);
        }
    }

    public static ChatComponent fromString(String text) {
        return new Builder().setText(text).translateColors().build();
    }

    private ChatComponent(String text, String translate, String color, ChatComponent[] extra, ChatComponent[] with) {
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

    public ChatComponent[] getExtra() {
        return extra;
    }

    public ChatComponent[] getWith() {
        return with;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
