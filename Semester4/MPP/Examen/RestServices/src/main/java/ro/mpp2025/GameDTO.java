package ro.mpp2025;

public class GameDTO {
    private String nickname;
    private int config;
    private int corect;

    public GameDTO(String nickname, int config, int corect) {
        this.nickname = nickname;
        this.config = config;
        this.corect = corect;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(int config) {
        this.config = config;
    }

    public int getCorect() {
        return corect;
    }

    public void setCorect(int corect) {
        this.corect = corect;
    }
}
