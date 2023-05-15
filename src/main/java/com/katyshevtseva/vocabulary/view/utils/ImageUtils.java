package com.katyshevtseva.vocabulary.view.utils;

import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.image.ImageContainer;
import com.katyshevtseva.image.ImageContainerCache;
import com.katyshevtseva.vocabulary.core.entity.Entry;

import java.io.File;
import java.util.Objects;

public class ImageUtils {
    private static final String IMAGE_LOCATION = "D:\\onedrive\\central_image_storage\\vocabulary\\";
    private static final ImageContainerCache icc = ImageContainerCache.getInstance();

    public static ImageContainer getImageContainerOrNull(Entry entry) {
        for (File file : Objects.requireNonNull(new File(IMAGE_LOCATION).listFiles())) {
            String fileNameWithoutExtension = file.getName().split("\\.")[0];//todo упростить
            Long id = Long.parseLong(fileNameWithoutExtension);
            if (id.equals(entry.getId())) {
                return icc.getImageContainer(file.getName(), IMAGE_LOCATION, 400);
            }
        }
        return null;
    }

    public static boolean hasExampleOrImage(Entry entry) {
        boolean hasExample = !GeneralUtils.isEmpty(entry.getExample());
        boolean hasImage = getImageContainerOrNull(entry) != null;
        return hasExample || hasImage;
    }
}
