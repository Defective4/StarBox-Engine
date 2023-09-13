package net.defekt.minecraft.starbox.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;

public class ChatComponent {
    private final String text;
    private final String translate;
    private final String color;
    private final ChatComponent[] extra;
    private final ChatComponent[] with;
    private final JsonObject hoverEvent;
    private final JsonObject clickEvent;

    public static class Builder {

        private String text;
        private String translate;
        private String color;
        private ChatComponent[] extra;
        private ChatComponent[] with;
        private JsonObject hoverEvent;
        private JsonObject clickEvent;

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

        public Builder addWith(ChatComponent component) {
            if (with == null) setWith(new ChatComponent[0]);
            with = Arrays.copyOf(with, with.length + 1);
            with[with.length - 1] = component;
            return this;
        }

        public Builder addExtra(ChatComponent component) {
            if (extra == null) setExtra(new ChatComponent[0]);
            extra = Arrays.copyOf(extra, extra.length + 1);
            extra[extra.length - 1] = component;
            return this;
        }

        public Builder translateColors() {
            text = text.replace("&", "§");
            return this;
        }

        public enum ClickEventType {
            OPEN_URL,
            RUN_COMMAND,
            SUGGEST_COMMAND,
            COPY_TO_CLIPBOARD
        }

        public Builder setClickEvent(ClickEventType event, String value) {
            clickEvent = new JsonObject();
            clickEvent.add("action", new JsonPrimitive(event.name().toLowerCase()));
            clickEvent.add("value", new JsonPrimitive(value));
            return this;
        }

        public Builder setHoverEvent(ChatComponent label) {
            hoverEvent = new JsonObject();
            hoverEvent.add("action", new JsonPrimitive("show_text"));
            hoverEvent.add("value", label.toJsonElement());
            return this;
        }

        public ChatComponent build() {
            return new ChatComponent(text,
                                     translate,
                                     color,
                                     extra,
                                     with,
                                     hoverEvent,
                                     clickEvent);
        }
    }

    private ChatComponent(String text, String translate, String color, ChatComponent[] extra, ChatComponent[] with, JsonObject hoverEvent, JsonObject clickEvent) {
        this.text = text;
        this.translate = translate;
        this.color = color;
        this.extra = extra;
        this.with = with;
        this.hoverEvent = hoverEvent;
        this.clickEvent = clickEvent;
    }

    public static ChatComponent fromString(String text) {
        return new Builder().setText(text).translateColors().build();
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

    public JsonElement toJsonElement() {
        return new Gson().toJsonTree(this);
    }

}
