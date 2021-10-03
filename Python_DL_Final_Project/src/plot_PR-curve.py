import cv2
import skimage
from skimage.transform import resize
import numpy as np
from tensorflow.compat.v1.keras import backend as K
import os

#Data preparation for test set
#re-label images into two categories

imageSize=112
test_dir = "OCT2017/test/"

from tqdm import tqdm
def get_data(folder):

    X = []
    y = []
    for folderName in os.listdir(folder):
        if not folderName.startswith('.'):
            if folderName in ['CNV']:
                label = 0
            elif folderName in ['DME']:
                label = 1
            elif folderName in ['DRUSEN']:
                label = 2
            elif folderName in ['NORMAL']:
                label = 3

            for image_filename in tqdm(os.listdir(folder + folderName)):
                img_file = cv2.imread(folder + folderName + '/' + image_filename)
                if img_file is not None:
                    img_file = skimage.transform.resize(img_file, (imageSize, imageSize, 3))
                    img_arr = np.asarray(img_file)
                    X.append(img_arr)
                    y.append(label)
    X = np.asarray(X)
    y = np.asarray(y)
    return X,y

x_test, y_test= get_data(test_dir)

from keras.utils.np_utils import to_categorical

y_testHot = to_categorical(y_test, num_classes = 4)


#load model
from tensorflow.compat.v1.keras import models
model=models.load_model('BNSepVGG.h5')
model.load_weights('BNSepVGG_best_weights.hdf5')

from keras import optimizers

model.compile(loss='categorical_crossentropy',
              optimizer='adam',
              metrics=['accuracy'])

pred=model.predict(x_test,y_test.any(),verbose=1)

from sklearn.preprocessing import label_binarize
n_classes=4
y_testb = label_binarize(y_test, classes=[i for i in range(n_classes)])

from sklearn.metrics import precision_recall_curve
from sklearn.metrics import average_precision_score

n_classes=4

# For each class
precision = dict()
recall = dict()
average_precision = dict()
for i in range(n_classes):
    precision[i], recall[i], _ = precision_recall_curve(y_testb[:, i],
                                                        pred[:, i])
    average_precision[i] = average_precision_score(y_testb[:, i], pred[:, i])

# A "micro-average": quantifying score on all classes jointly
precision["micro"], recall["micro"], _ = precision_recall_curve(y_testb.ravel(),
    pred.ravel())
average_precision["micro"] = average_precision_score(y_testb, pred,
                                                     average="micro")

from itertools import cycle
# setup plot details
colors = cycle(['turquoise', 'darkorange', 'cornflowerblue','gold', 'teal'])

plt.figure(figsize=(7, 8))
f_scores = np.linspace(0.2, 0.8, num=4)
lines = []
labels = []
for f_score in f_scores:
    x = np.linspace(0.01, 1)
    y = f_score * x / (2 * x - f_score)
    l, = plt.plot(x[y >= 0], y[y >= 0], color='gray', alpha=0.2)
    plt.annotate('f1={0:0.1f}'.format(f_score), xy=(0.9, y[45] + 0.02))

lines.append(l)
labels.append('iso-f1 curves')

for i, color in zip(range(n_classes), colors):
    l, = plt.plot(recall[i], precision[i], color=color, lw=2)
    lines.append(l)
    if i == 0:
      labels.append('Precision-recall for CNV (area = {1:0.4f})'
                    ''.format(i, average_precision[i]))
    elif i == 1:
      labels.append('Precision-recall for DME (area = {1:0.4f})'
                    ''.format(i, average_precision[i]))
    elif i == 2:
      labels.append('Precision-recall for DRUSEN (area = {1:0.4f})'
                    ''.format(i, average_precision[i]))
    else:
      labels.append('Precision-recall for NORMAL (area = {1:0.4f})'
                    ''.format(i, average_precision[i]))
fig = plt.gcf()
fig.subplots_adjust(bottom=0.25)
plt.xlim([0.0, 1.0])
plt.ylim([0.0, 1.05])
plt.xlabel('Recall')
plt.ylabel('Precision')
plt.title('Extension of Precision-Recall curve to multi-class')
plt.legend(lines, labels, loc="lower left", prop=dict(size=12))


plt.show()