from tensorflow.compat.v1.keras.preprocessing.image import ImageDataGenerator
from tensorflow.compat.v1.keras.models import Sequential, load_model
from tensorflow.compat.v1.keras.layers import Activation, Dropout, Flatten, Reshape, Dense, Concatenate, GlobalMaxPooling2D
from tensorflow.compat.v1.keras.layers import BatchNormalization, Input, Conv2D, Lambda, Average
from tensorflow.compat.v1.keras.applications.inception_v3 import InceptionV3
from tensorflow.compat.v1.keras.callbacks import ModelCheckpoint
from tensorflow.compat.v1.keras import metrics
from tensorflow.compat.v1.keras.optimizers import Adam
from tensorflow.compat.v1.keras import backend as K
import tensorflow.compat.v1.keras
from tensorflow.compat.v1.keras.models import Model

NUM_CLASSES = 4
WINDOW_SIZE = 112
IMAGE_CHANNELS = 3
    
def create_model(n_out):
    input_shape=(WINDOW_SIZE,WINDOW_SIZE, IMAGE_CHANNELS)
    input_tensor = Input(shape=(WINDOW_SIZE, WINDOW_SIZE, IMAGE_CHANNELS))

    #load conv base of InceptionV3 with pretrained weights on imageNet
    base_model = InceptionV3(include_top=False,
                             weights='imagenet',
                             input_shape=input_shape
                            )
    bn = BatchNormalization()(input_tensor)
    x = base_model(bn)

    #add full connected layers
    x = Flatten()(x)
    x = Dense(4096, activation='relu')(x)
    output = Dense(n_out, activation='softmax')(x)
    model = Model(input_tensor, output)
    
    return model

#create model
model = create_model(n_out=NUM_CLASSES)

#freeze untrainable layers
for layer in model.layers:
    layer.trainable = False

model.layers[-1].trainable = True
model.layers[-2].trainable = True
model.layers[-3].trainable = True

import tensorflow.compat.v1 as tf
from tensorflow.compat.v1.keras import layers
from tensorflow.compat.v1.keras import models
from tensorflow.compat.v1.keras.callbacks import ModelCheckpoint, ReduceLROnPlateau
from tensorflow.compat.v1.keras import backend as K
import os

#target directory
dataset_dir = 'OCT2017/'
train_dir = os.path.join(dataset_dir, 'train')
val_dir = os.path.join(dataset_dir, 'val')

#compile the model
model.compile(loss='categorical_crossentropy',
              optimizer='adam',
              metrics=['accuracy'])

from keras.preprocessing.image import ImageDataGenerator

train_datagen = ImageDataGenerator(
    rescale=1./255)


train_generator = train_datagen.flow_from_directory(
        # This is the target directory
        train_dir,
        # All images will be resized to 100x100
        target_size=(112, 112),
        batch_size=100,
        # Since we use binary_crossentropy loss, we need binary labels
        class_mode='categorical')

test_datagen = ImageDataGenerator(rescale=1./255)

validation_generator = test_datagen.flow_from_directory(
        val_dir,
        target_size=(112, 112),
        batch_size=100,
        class_mode='categorical')

#callbacks
checkpoint = ModelCheckpoint(filepath='TFInceptionV3_best_weights.hdf5', save_best_only=True, save_weights_only=True)
lr_reduce = ReduceLROnPlateau(monitor='val_loss', factor=0.3, patience=2, verbose=2, mode='max')

#fit the model with generator
history = model.fit_generator(
      train_generator,
      steps_per_epoch=759,
      epochs=20,
      validation_data=validation_generator,
      validation_steps=5,
      callbacks=[checkpoint,lr_reduce])

model.save('TFInceptionV3.h5')

#plot accuracies and losses at each epochs during training
import matplotlib.pyplot as plt

acc = history.history['accuracy']
val_acc = history.history['val_accuracy']
loss = history.history['loss']
val_loss = history.history['val_loss']

epochs = range(len(acc))

plt.plot(epochs, acc, 'bo', label='Training accuracy')
plt.plot(epochs, val_acc, 'b', label='Validation accuracy')
plt.title('Training and validation accuracy')
plt.legend()
plt.savefig('TFInceptionV3_acc.png', format = 'png')

plt.figure()

plt.plot(epochs, loss, 'bo', label='Training loss')
plt.plot(epochs, val_loss, 'b', label='Validation loss')
plt.title('Training and validation loss')
plt.legend()
plt.savefig('TFInceptionV3_loss.png', format = 'png')
