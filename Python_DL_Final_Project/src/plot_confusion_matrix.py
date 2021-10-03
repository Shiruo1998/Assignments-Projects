import cv2
import skimage
from skimage.transform import resize
import numpy as np
from tensorflow.compat.v1.keras import backend as K
import os

#data preparation
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
#compile the model
model.compile(loss='categorical_crossentropy',
              optimizer='adam',
              metrics=['accuracy'])

#evaluate the model
test_loss, test_acc = model.evaluate(x_test,y_testHot,batch_size=500)
print(test_loss)
print(test_acc)

#predict on the test set
pred=model.predict(x_test,y_test.any(),verbose=1)
import numpy as np
predict_label=np.argmax(pred, 1)

#plot confusion matrix
import pandas as pd
pd.crosstab(y_test,predict_label,rownames=['true_label'],colnames=['predict_label'])