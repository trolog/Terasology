/*
 * Copyright 2013 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.rendering.primitives;

import org.terasology.asset.AssetUri;
import org.terasology.engine.API;
import org.terasology.math.Rect2i;
import org.terasology.rendering.assets.mesh.Mesh;
import org.terasology.rendering.assets.texture.TextureRegion;

import javax.vecmath.Vector4f;
import java.nio.ByteBuffer;

@API
public final class MeshFactory {

    private MeshFactory() {
    }

    public static Mesh generateItemMesh(AssetUri uri, TextureRegion tex) {
        return generateItemMesh(uri, tex, 0, false, null);
    }

    public static Mesh generateItemMesh(AssetUri uri, TextureRegion tex, int alphaLimit, boolean withContour, Vector4f colorContour) {
        ByteBuffer buffer = tex.getTexture().getData().getBuffers()[0];

        Rect2i pixelRegion = tex.getPixelRegion();
        int posX = pixelRegion.minX();
        int posY = pixelRegion.minY();

        int stride = tex.getTexture().getWidth() * 4;

        float textureSize = Math.max(tex.getWidth(), tex.getHeight());

        Tessellator tessellator = new Tessellator();

        for (int y = 0; y < tex.getHeight(); y++) {
            for (int x = 0; x < tex.getWidth(); x++) {
                int r = buffer.get((posY + y) * stride + (posX + x) * 4) & 255;
                int g = buffer.get((posY + y) * stride + (posX + x) * 4 + 1) & 255;
                int b = buffer.get((posY + y) * stride + (posX + x) * 4 + 2) & 255;
                int a = buffer.get((posY + y) * stride + (posX + x) * 4 + 3) & 255;

                if (a > alphaLimit) {
                    Vector4f color = new Vector4f(r / 255f, g / 255f, b / 255f, a / 255f);
                    TessellatorHelper.addBlockMesh(tessellator, color, 2f / textureSize, 1.0f, 0.5f, 2f / textureSize * x - 0.5f, 2f / textureSize * (15 - y) - 1f, 0f);

                    if (withContour) {
                        int newX = 0;
                        int newY = 0;
                        int newA = 0;

                        for (int i = 0; i < 4; i++) {
                            newA = alphaLimit + 1;
                            switch (i) {
                                case 0:
                                    //check left
                                    if (x > 0) {
                                        newX = x - 1;
                                        newY = y;
                                        newA = buffer.get((posY + newY) * stride + (posX + newX) * 4 + 3) & 255;
                                    }
                                    break;
                                case 1:
                                    //check top
                                    if (y > 0) {
                                        newX = x;
                                        newY = y - 1;
                                        newA = buffer.get((posY + newY) * stride + (posX + newX) * 4 + 3) & 255;
                                    }
                                    break;
                                case 2:
                                    //check right
                                    if (x < 16) {
                                        newX = x + 1;
                                        newY = y;
                                        newA = buffer.get((posY + newY) * stride + (posX + newX) * 4 + 3) & 255;
                                    }
                                    break;
                                case 3:
                                    //check bottom
                                    if (y < 16) {
                                        newX = x;
                                        newY = y + 1;
                                        newA = buffer.get((posY + newY) * stride + (posX + newX) * 4 + 3) & 255;
                                    }
                                    break;
                            }

                            if (newA < alphaLimit) {
                                Vector4f cColor = new Vector4f(colorContour.x / 255f, colorContour.y / 255f, colorContour.z / 255f, colorContour.w);
                                TessellatorHelper.addBlockMesh(tessellator, cColor, 0.125f, 1.0f, 0.5f, 2f * 0.0625f * newX - 0.5f, 0.125f * (15 - newY) - 1f, 0f);
                            }
                        }
                    }
                }
            }
        }

        return tessellator.generateMesh(uri);
    }

}
