import cv2
import skimage
from skimage.transform import resize
import numpy as np
from tensorflow.compat.v1.keras import backend as K
import os

imageSize=112

#Data preparation for test set
#re-label images into two categories
test_dir = "OCT/test/"
from tqdm import tqdm
def get_data(folder):

    X = []
    y = []
    for folderName in os.listdir(folder):
        if not folderName.startswith('.'):
            if folderName in ['CNV']:
                label = 1
            elif folderName in ['DME']:
                label = 1
            elif folderName in ['DRUSEN']:
                label = 0
            elif folderName in ['NORMAL']:
                label = 0

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


from tensorflow.compat.v1.keras import models

#load models to be compared
model=models.load_model('TFDenseNet.h5')
model.load_weights('TFDenseNet_best_weights.hdf5')

#give prediction in test set
predD=model.predict(x_test,y_test.any(),verbose=1)

model=models.load_model('TFResNet50.h5')
model.load_weights('TFResNet50_best_weights.hdf5')

predR=model.predict(x_test,y_test.any(),verbose=1)

model=models.load_model('TFInceptionV3.h5')
model.load_weights('TFInceptionV3_best_weights.hdf5')

predI=model.predict(x_test,y_test.any(),verbose=1)

model=models.load_model('TFXception.h5')
model.load_weights('TFXception.hdf5')

predX=model.predict(x_test,y_test.any(),verbose=1)

model=models.load_model('drive/0407/ABNSepVGG.h5')
model.load_weights('drive/0407/BNSepVGG_best_weights.hdf5')

predB=model.predict(x_test,y_test.any(),verbose=1)

model=models.load_model('drive/VGG16.h5')
model.load_weights('drive/VGG16_best_weights.hdf5')

predV=model.predict(x_test,y_test.any(),verbose=1)

model=models.load_model('drive/0409/Alexnet.h5')
model.load_weights('drive/0409/Alexnet_best_weights.hdf5')

predA = model.predict(x_test,y_test.any(),verbose=1)

#reform the prediction outcome into two categories
p = np.zeros((500,7))
p[:, 0] = predB[:, 0] + predB[:,1]
p[:, 1] = predV[:, 0] + predV[:,1]
p[:, 2] = predA[:, 0] + predA[:,1]
p[:, 3] = predD[:, 0] + predD[:,1]
p[:, 4] = predR[:, 0] + predR[:,1]
p[:, 5] = predI[:, 0] + predI[:,1]
p[:, 6] = predX[:, 0] + predX[:,1]

#generate a (500, 7) array for evaluating the seven models' outcome
y = np.zeros((500,7))
y[:, 0] = y_test
y[:, 1] = y_test
y[:, 2] = y_test
y[:, 3] = y_test
y[:, 4] = y_test
y[:, 5] = y_test
y[:, 6] = y_test


import numpy as np
import matplotlib.pyplot as plt
from itertools import cycle
from sklearn import svm, datasets
from sklearn.metrics import roc_curve, auc
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import label_binarize
from sklearn.multiclass import OneVsRestClassifier
from scipy import interp


n_models = 7

y_testb = label_binarize(y, classes=[i for i in range(n_models)])

fpr = dict()
tpr = dict()
roc_auc = dict()
a = np.zeros((500,1))
b = np.zeros((500,1))

for i in range(n_models):
    a = y_testb[:,i]
    b = p[:, i]
    fpr[i], tpr[i], _ = roc_curve(a, b)
    roc_auc[i] = auc(fpr[i], tpr[i])


# Plot all ROC curves
lw=2
plt.figure()

colors = cycle(['teal', 'navy', 'cornflowerblue', 'aqua', 'deeppink', 'gold', 'darkorange'])
for i, color in zip(range(n_models), colors):
    if i == 0:
        plt.plot(fpr[i], tpr[i], color=color, lw=lw,
                 label='ROC curve of BNSepVGG (area = {1:0.4f})'
                 ''.format(i, roc_auc[i]))
    elif i == 1:
        plt.plot(fpr[i], tpr[i], color=color, lw=lw,
                 label='ROC curve of VGG16 (area = {1:0.4f})'
                 ''.format(i, roc_auc[i]))
    elif i == 2:
        plt.plot(fpr[i], tpr[i], color=color, lw=lw,
                 label='ROC curve of AlexNet (area = {1:0.4f})'
                 ''.format(i, roc_auc[i]))
    elif i == 3:
        plt.plot(fpr[i], tpr[i], color=color, lw=lw,
                 label='ROC curve of pretrained DenseNet201 (area = {1:0.4f})'
                 ''.format(i, roc_auc[i]))
    elif i == 4:
        plt.plot(fpr[i], tpr[i], color=color, lw=lw,
                 label='ROC curve of pretrained ResNet50 (area = {1:0.4f})'
                 ''.format(i, roc_auc[i]))
    elif i == 5:
        plt.plot(fpr[i], tpr[i], color=color, lw=lw,
                 label='ROC curve of pretrained InceptionV3 (area = {1:0.4f})'
                 ''.format(i, roc_auc[i]))
    elif i == 6:
        plt.plot(fpr[i], tpr[i], color=color, lw=lw,
                 label='ROC curve of pretrained Xception (area = {1:0.4f})'
                 ''.format(i, roc_auc[i]))

plt.plot([0, 1], [0, 1], 'k--', lw=lw)
plt.xlim([0.0, 1.0])
plt.ylim([0.0, 1.05])
plt.xlabel('False Positive Rate')
plt.ylabel('True Positive Rate')
plt.title('Some extension of Receiver operating characteristic to multi-class')
plt.legend(loc="lower right")
plt.show()
