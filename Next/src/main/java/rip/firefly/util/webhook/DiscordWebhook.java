package rip.firefly.util.webhook;

import javax.net.ssl.HttpsURLConnection;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class used to execute Discord Webhooks with low effort
 * Come from: https://gist.github.com/k3kdude/fba6f6b37594eae3d6f9475330733bdb
 */
public class DiscordWebhook {

    private final String url;
    private String content;
    private String username;
    private String avatarUrl;
    private boolean tts;
    private List<EmbedObject> embeds = new ArrayList<>();

    /**
     * Constructs a new DiscordWebhook instance
     *
     * @param url The webhook URL obtained in Discord
     */
    public DiscordWebhook(String url) {
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setTts(boolean tts) {
        this.tts = tts;
    }

    public void addEmbed(EmbedObject embed) {
        this.embeds.add(embed);
    }

    public void execute() throws IOException {
        if (this.content == null && this.embeds.isEmpty()) {
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
        }

        JSONObject json = new JSONObject();

        json.put("content", this.content);
        json.put("username", this.username);
        json.put("avatar_url", this.avatarUrl);
        json.put("tts", this.tts);

        if (!this.embeds.isEmpty()) {
            List<JSONObject> embedObjects = new ArrayList<>();

            for (EmbedObject embed : this.embeds) {
                JSONObject jsonEmbed = new JSONObject();

                jsonEmbed.put("title", embed.getTitle());
                jsonEmbed.put("description", embed.getDescription());
                jsonEmbed.put("url", embed.getUrl());

                if (embed.getColor() != null) {
                    Color color = embed.getColor();
                    int rgb = color.getRed();
                    rgb = (rgb << 8) + color.getGreen();
                    rgb = (rgb << 8) + color.getBlue();

                    jsonEmbed.put("color", rgb);
                }

                EmbedObject.Footer footer = embed.getFooter();
                EmbedObject.Image image = embed.getImage();
                EmbedObject.Thumbnail thumbnail = embed.getThumbnail();
                EmbedObject.Author author = embed.getAuthor();
                List<EmbedObject.Field> fields = embed.getFields();

                if (footer != null) {
                    JSONObject jsonFooter = new JSONObject();

                    jsonFooter.put("text", footer.getText());
                    jsonFooter.put("icon_url", footer.getIconUrl());
                    jsonEmbed.put("footer", jsonFooter);
                }

                if (image != null) {
                    JSONObject jsonImage = new JSONObject();

                    jsonImage.put("url", image.getUrl());
                    jsonEmbed.put("image", jsonImage);
                }

                if (thumbnail != null) {
                    JSONObject jsonThumbnail = new JSONObject();

                    jsonThumbnail.put("url", thumbnail.getUrl());
                    jsonEmbed.put("thumbnail", jsonThumbnail);
                }

                if (author != null) {
                    JSONObject jsonAuthor = new JSONObject();

                    jsonAuthor.put("name", author.getName());
                    jsonAuthor.put("url", author.getUrl());
                    jsonAuthor.put("icon_url", author.getIconUrl());
                    jsonEmbed.put("author", jsonAuthor);
                }

                List<JSONObject> jsonFields = new ArrayList<>();
                for (EmbedObject.Field field : fields) {
                    JSONObject jsonField = new JSONObject();

                    jsonField.put("name", field.getName());
                    jsonField.put("value", field.getValue());
                    jsonField.put("inline", field.isInline());

                    jsonFields.add(jsonField);
                }

                jsonEmbed.put("fields", jsonFields.toArray());
                embedObjects.add(jsonEmbed);
            }

            json.put("embeds", embedObjects.toArray());
        }

        URL url = new URL(this.url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStream stream = connection.getOutputStream();
        stream.write(json.toString().getBytes());
        stream.flush();
        stream.close();

        connection.getInputStream().close(); //I'm not sure why but it doesn't work without getting the InputStream
        connection.disconnect();
    }

    public static class EmbedObject {
        private String title;
        private String description;
        private String url;
        private Color color;

        private Footer footer;
        private Thumbnail thumbnail;
        private Image image;
        private Author author;
        private List<Field> fields = new ArrayList<>();

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getUrl() {
            return url;
        }

        public Color getColor() {
            return color;
        }

        public Footer getFooter() {
            return footer;
        }

        public Thumbnail getThumbnail() {
            return thumbnail;
        }

        public Image getImage() {
            return image;
        }

        public Author getAuthor() {
            return author;
        }

        public List<Field> getFields() {
            return fields;
        }

        public EmbedObject setTitle(String title) {
            this.title = title;
            return this;
        }

        public EmbedObject setDescription(String description) {
            this.description = description;
            return this;
        }

        public EmbedObject setUrl(String url) {
            this.url = url;
            return this;
        }

        public EmbedObject setColor(Color color) {
            this.color = color;
            return this;
        }

        public EmbedObject setFooter(String text, String icon) {
            this.footer = new Footer(text, icon);
            return this;
        }

        public EmbedObject setThumbnail(String url) {
            this.thumbnail = new Thumbnail(url);
            return this;
        }

        public EmbedObject setImage(String url) {
            this.image = new Image(url);
            return this;
        }

        public EmbedObject setAuthor(String name, String url, String icon) {
            this.author = new Author(name, url, icon);
            return this;
        }

        public EmbedObject addField(String name, String value, boolean inline) {
            this.fields.add(new Field(name, value, inline));
            return this;
        }

        private class Footer {
            private String text;
            private String iconUrl;

            private Footer(String text, String iconUrl) {
                this.text = text;
                this.iconUrl = iconUrl;
            }

            private String getText() {
                return text;
            }

            private String getIconUrl() {
                return iconUrl;
            }
        }

        private class Thumbnail {
            private String url;

            private Thumbnail(String url) {
                this.url = url;
            }

            private String getUrl() {
                return url;
            }
        }

        private class Image {
            private String url;

            private Image(String url) {
                this.url = url;
            }

            private String getUrl() {
                return url;
            }
        }

        private class Author {
            private String name;
            private String url;
            private String iconUrl;

            private Author(String name, String url, String iconUrl) {
                this.name = name;
                this.url = url;
                this.iconUrl = iconUrl;
            }

            private String getName() {
                return name;
            }

            private String getUrl() {
                return url;
            }

            private String getIconUrl() {
                return iconUrl;
            }
        }

        private class Field {
            private String name;
            private String value;
            private boolean inline;

            private Field(String name, String value, boolean inline) {
                this.name = name;
                this.value = value;
                this.inline = inline;
            }

            private String getName() {
                return name;
            }

            private String getValue() {
                return value;
            }

            private boolean isInline() {
                return inline;
            }
        }
    }

    private class JSONObject {

        private final HashMap<String, Object> map = new HashMap<>();

        void put(String key, Object value) {
            if (value != null) {
                map.put(key, value);
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            builder.append("{");

            int i = 0;
            for (Map.Entry<String, Object> entry : entrySet) {
                Object val = entry.getValue();
                builder.append(quote(entry.getKey())).append(":");

                if (val instanceof String) {
                    builder.append(quote(String.valueOf(val)));
                } else if (val instanceof Integer) {
                    builder.append(Integer.valueOf(String.valueOf(val)));
                } else if (val instanceof Boolean) {
                    builder.append(val);
                } else if (val instanceof JSONObject) {
                    builder.append(val.toString());
                } else if (val.getClass().isArray()) {
                    builder.append("[");
                    int len = Array.getLength(val);
                    for (int j = 0; j < len; j++) {
                        builder.append(Array.get(val, j).toString()).append(j != len - 1 ? "," : "");
                    }
                    builder.append("]");
                }

                builder.append(++i == entrySet.size() ? "}" : ",");
            }

            return builder.toString();
        }

        private String quote(String string) {
            return "\"" + string + "\"";
        }
    }
}

//import javax.net.ssl.HttpsURLConnection;
//import java.awt.*;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.lang.reflect.Array;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.*;
//
///**
// * @author kyleonaut
// * @version 1.0.0
// * created at 01.11.2021
// */
//import java.util.Set;
//import java.lang.reflect.Array;
//import java.util.Map;
//import java.util.HashMap;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.awt.Color;
//import java.util.Iterator;
//import javax.net.ssl.HttpsURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//public class DiscordWebhook
//{
//    private final String url;
//    private String content;
//    private String username;
//    private String avatarUrl;
//    private boolean tts;
//    private final List<EmbedObject> embeds;
//
//    public DiscordWebhook(final String url) {
//        this.embeds = new ArrayList<EmbedObject>();
//        this.url = url;
//    }
//
//    public void setContent(final String content) {
//        this.content = content;
//    }
//
//    public void setUsername(final String username) {
//        this.username = username;
//    }
//
//    public void setAvatarUrl(final String avatarUrl) {
//        this.avatarUrl = avatarUrl;
//    }
//
//    public void setTts(final boolean tts) {
//        this.tts = tts;
//    }
//
//    public void addEmbed(final EmbedObject embed) {
//        this.embeds.add(embed);
//    }
//
//    public void execute() throws IOException {
//        if (this.content == null && this.embeds.isEmpty()) {
//            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
//        }
//        final JSONObject json = new JSONObject();
//        json.put("content", this.content);
//        json.put("username", this.username);
//        json.put("avatar_url", this.avatarUrl);
//        json.put("tts", this.tts);
//        if (!this.embeds.isEmpty()) {
//            final List<JSONObject> embedObjects = new ArrayList<JSONObject>();
//            for (final EmbedObject embed : this.embeds) {
//                final JSONObject jsonEmbed = new JSONObject();
//                jsonEmbed.put("title", embed.getTitle());
//                jsonEmbed.put("description", embed.getDescription());
//                jsonEmbed.put("url", embed.getUrl());
//                if (embed.getColor() != null) {
//                    final Color color = embed.getColor();
//                    int rgb = color.getRed();
//                    rgb = (rgb << 8) + color.getGreen();
//                    rgb = (rgb << 8) + color.getBlue();
//                    jsonEmbed.put("color", rgb);
//                }
//                final EmbedObject.Footer footer = embed.getFooter();
//                final EmbedObject.Image image = embed.getImage();
//                final EmbedObject.Thumbnail thumbnail = embed.getThumbnail();
//                final EmbedObject.Author author = embed.getAuthor();
//                final List<EmbedObject.Field> fields = embed.getFields();
//                if (footer != null) {
//                    final JSONObject jsonFooter = new JSONObject();
//                    jsonFooter.put("text", footer.getText());
//                    jsonFooter.put("icon_url", footer.getIconUrl());
//                    jsonEmbed.put("footer", jsonFooter);
//                }
//                if (image != null) {
//                    final JSONObject jsonImage = new JSONObject();
//                    jsonImage.put("url", image.getUrl());
//                    jsonEmbed.put("image", jsonImage);
//                }
//                if (thumbnail != null) {
//                    final JSONObject jsonThumbnail = new JSONObject();
//                    jsonThumbnail.put("url", thumbnail.getUrl());
//                    jsonEmbed.put("thumbnail", jsonThumbnail);
//                }
//                if (author != null) {
//                    final JSONObject jsonAuthor = new JSONObject();
//                    jsonAuthor.put("name", author.getName());
//                    jsonAuthor.put("url", author.getUrl());
//                    jsonAuthor.put("icon_url", author.getIconUrl());
//                    jsonEmbed.put("author", jsonAuthor);
//                }
//                final List<JSONObject> jsonFields = new ArrayList<JSONObject>();
//                for (final EmbedObject.Field field : fields) {
//                    final JSONObject jsonField = new JSONObject();
//                    jsonField.put("name", field.getName());
//                    jsonField.put("value", field.getValue());
//                    jsonField.put("inline", field.isInline());
//                    jsonFields.add(jsonField);
//                }
//                jsonEmbed.put("fields", jsonFields.toArray());
//                embedObjects.add(jsonEmbed);
//            }
//            json.put("embeds", embedObjects.toArray());
//        }
//        final URL url = new URL(this.url);
//        final HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
//        connection.addRequestProperty("Content-Type", "application/json");
//        connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
//        connection.setDoOutput(true);
//        connection.setRequestMethod("POST");
//        final OutputStream stream = connection.getOutputStream();
//        stream.write(json.toString().getBytes());
//        stream.flush();
//        stream.close();
//        connection.getInputStream().close();
//        connection.disconnect();
//    }
//
//    public static class EmbedObject
//    {
//        private String title;
//        private String description;
//        private String url;
//        private Color color;
//        private Footer footer;
//        private Thumbnail thumbnail;
//        private Image image;
//        private Author author;
//        private final List<Field> fields;
//
//        public EmbedObject() {
//            this.fields = new ArrayList<Field>();
//        }
//
//        public String getTitle() {
//            return this.title;
//        }
//
//        public String getDescription() {
//            return this.description;
//        }
//
//        public String getUrl() {
//            return this.url;
//        }
//
//        public Color getColor() {
//            return this.color;
//        }
//
//        public Footer getFooter() {
//            return this.footer;
//        }
//
//        public Thumbnail getThumbnail() {
//            return this.thumbnail;
//        }
//
//        public Image getImage() {
//            return this.image;
//        }
//
//        public Author getAuthor() {
//            return this.author;
//        }
//
//        public List<Field> getFields() {
//            return this.fields;
//        }
//
//        public EmbedObject setTitle(final String title) {
//            this.title = title;
//            return this;
//        }
//
//        public EmbedObject setDescription(final String description) {
//            this.description = description;
//            return this;
//        }
//
//        public EmbedObject setUrl(final String url) {
//            this.url = url;
//            return this;
//        }
//
//        public EmbedObject setColor(final Color color) {
//            this.color = color;
//            return this;
//        }
//
//        public EmbedObject setFooter(final String text, final String icon) {
//            this.footer = new Footer(text, icon);
//            return this;
//        }
//
//        public EmbedObject setThumbnail(final String url) {
//            this.thumbnail = new Thumbnail(url);
//            return this;
//        }
//
//        public EmbedObject setImage(final String url) {
//            this.image = new Image(url);
//            return this;
//        }
//
//        public EmbedObject setAuthor(final String name, final String url, final String icon) {
//            this.author = new Author(name, url, icon);
//            return this;
//        }
//
//        public EmbedObject addField(final String name, final String value, final boolean inline) {
//            this.fields.add(new Field(name, value, inline));
//            return this;
//        }
//
//        private class Footer
//        {
//            private final String text;
//            private final String iconUrl;
//
//            private Footer(final String text, final String iconUrl) {
//                this.text = text;
//                this.iconUrl = iconUrl;
//            }
//
//            private String getText() {
//                return this.text;
//            }
//
//            private String getIconUrl() {
//                return this.iconUrl;
//            }
//        }
//
//        private class Thumbnail
//        {
//            private final String url;
//
//            private Thumbnail(final String url) {
//                this.url = url;
//            }
//
//            private String getUrl() {
//                return this.url;
//            }
//        }
//
//        private class Image
//        {
//            private final String url;
//
//            private Image(final String url) {
//                this.url = url;
//            }
//
//            private String getUrl() {
//                return this.url;
//            }
//        }
//
//        private class Author
//        {
//            private final String name;
//            private final String url;
//            private final String iconUrl;
//
//            private Author(final String name, final String url, final String iconUrl) {
//                this.name = name;
//                this.url = url;
//                this.iconUrl = iconUrl;
//            }
//
//            private String getName() {
//                return this.name;
//            }
//
//            private String getUrl() {
//                return this.url;
//            }
//
//            private String getIconUrl() {
//                return this.iconUrl;
//            }
//        }
//
//        private class Field
//        {
//            private final String name;
//            private final String value;
//            private final boolean inline;
//
//            private Field(final String name, final String value, final boolean inline) {
//                this.name = name;
//                this.value = value;
//                this.inline = inline;
//            }
//
//            private String getName() {
//                return this.name;
//            }
//
//            private String getValue() {
//                return this.value;
//            }
//
//            private boolean isInline() {
//                return this.inline;
//            }
//        }
//    }
//
//    private class JSONObject
//    {
//        private final HashMap<String, Object> map;
//
//        private JSONObject() {
//            this.map = new HashMap<String, Object>();
//        }
//
//        void put(final String key, final Object value) {
//            if (value != null) {
//                this.map.put(key, value);
//            }
//        }
//
//        @Override
//        public String toString() {
//            final StringBuilder builder = new StringBuilder();
//            final Set<Map.Entry<String, Object>> entrySet = this.map.entrySet();
//            builder.append("{");
//            int i = 0;
//            for (final Map.Entry<String, Object> entry : entrySet) {
//                final Object val = entry.getValue();
//                builder.append(this.quote(entry.getKey())).append(":");
//                if (val instanceof String) {
//                    builder.append(this.quote(String.valueOf(val)));
//                }
//                else if (val instanceof Integer) {
//                    builder.append(Integer.valueOf(String.valueOf(val)));
//                }
//                else if (val instanceof Boolean) {
//                    builder.append(val);
//                }
//                else if (val instanceof JSONObject) {
//                    builder.append(val);
//                }
//                else if (val.getClass().isArray()) {
//                    builder.append("[");
//                    for (int len = Array.getLength(val), j = 0; j < len; ++j) {
//                        builder.append(Array.get(val, j).toString()).append((j != len - 1) ? "," : "");
//                    }
//                    builder.append("]");
//                }
//                builder.append((++i == entrySet.size()) ? "}" : ",");
//            }
//            return builder.toString();
//        }
//
//        private String quote(final String string) {
//            return "\"" + string + "\"";
//        }
//    }
//}