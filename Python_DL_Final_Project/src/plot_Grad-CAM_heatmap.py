from tensorflow.compat.v1.keras import models

#load model
model = models.load_model('BNSepVGG.h5')
model.load_weights('BNSepVGG_best_weights.hdf5')

from keras.preprocessing import image
from keras.models import load_model
from keras import backend as K
import matplotlib.pyplot as plt
import numpy as np

import skimage
from skimage.transform import resize

import cv2
# The local path to our target image
img_path = 'OCT2017/test/CNV/CNV-2177326-1.jpeg'

img_file = cv2.imread(img_path)

img_file = skimage.transform.resize(img_file, (112, 112, 3))
x = np.asarray(img_file)

# Add a dimension to transform our array into a "batch"
# of size (1, 112, 112, 3)
x = np.expand_dims(x, axis=0)



# This is the "CNV" entry in the prediction vector
CNV_output = model.output[:, 0]

# The is the output feature map of the `max_pooling2d_3` layer,
last_conv_layer = model.get_layer('max_pooling2d_3')

# This is the gradient of the "CNV" class with regard to
# the output feature map of `max_pooling2d_3`
grads = K.gradients(CNV_output, last_conv_layer.output)[0]

# This is a vector of shape (512,), where each entry
# is the mean intensity of the gradient over a specific feature map channel
pooled_grads = K.mean(grads, axis=(0, 1, 2))

# This function allows us to access the values of the quantities we just defined:
# `pooled_grads` and the output feature map of `max_pooling2d_3`,
# given a sample image
iterate = K.function([model.input], [CNV_output,pooled_grads, last_conv_layer.output[0]])

# These are the values of these two quantities, as Numpy arrays,
# given our sample image of two elephants
outputt,pooled_grads_value, conv_layer_output_value = iterate([x])

# We multiply each channel in the feature map array
# by "how important this channel is" with regard to the elephant class
for i in range(512):
    conv_layer_output_value[:, :, i] *= pooled_grads_value[i]

# The channel-wise mean of the resulting feature map
# is our heatmap of class activation
heatmap = np.mean(conv_layer_output_value, axis=-1)


heatmap = np.maximum(heatmap, 0)

heatmap /= np.max(heatmap)

heatmap[np.where(heatmap < 0.6)] = 0
plt.matshow(heatmap)

import cv2

# Use cv2 to load the original image
img = cv2.imread(img_path)

# Resize the heatmap to have the same size as the original image
heatmap = cv2.resize(heatmap, (img.shape[1], img.shape[0]))

# Convert the heatmap to RGB
heatmap = np.uint8(255 * heatmap)

# Apply the heatmap to the original image
heatmap = cv2.applyColorMap(heatmap, cv2.COLORMAP_JET)

# 0.4 here is a heatmap intensity factor
superimposed_img = heatmap * 0.4 + img

# Save the image to disk
cv2.imwrite('hm-CNV-2177326-1.jpeg', superimposed_img)