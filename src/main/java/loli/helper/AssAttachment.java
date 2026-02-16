package loli.helper;

import loli.enumeration.AssAttachmentType;

public class AssAttachment {

    private String path;
    private AssAttachmentType type;

    public AssAttachment(String path, AssAttachmentType type) {
        this.path = path;
        this.type = type;
    }

    public AssAttachment() {
        path = null;
        type = AssAttachmentType.None;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public AssAttachmentType getType() {
        return type;
    }

    public void setType(AssAttachmentType type) {
        this.type = type;
    }
}
