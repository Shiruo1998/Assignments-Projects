import os

import tensorflow.compat.v1 as tf
from tensorflow.compat.v1.keras import layers
from tensorflow.compat.v1.keras import models
from tensorflow.compat.v1.keras.callbacks import ModelCheckpoint, ReduceLROnPlateau
from tensorflow.compat.v1.keras import backend as K

from tensorflow.compat.v1.keras.applications.xception import Xception

#target directory
dataset_dir = 'OCT2017/'
train_dir = os.path.join(dataset_dir, 'train')
val_dir = os.path.join(dataset_dir, 'val')

#load conv base of Xception and weights pretrained on ImageNet
conv_base = Xception(weights='imagenet', include_top=False, input_shape=(112, 112, 3))
input_tensor = Input(shape=(112, 112, 3))
x = conv_base(input_tensor)

#add full connected layers
x = Flatten()(x)
x = Dense(4096, activation='relu')(x)
output = Dense(4, activation='softmax')(x)
model = Model(input_tensor, output)

conv_base.trainable = False

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
        # All images will be resized to 112x112
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
checkpoint = ModelCheckpoint(filepath='TFXception.hdf5', save_best_only=True, save_weights_only=True)
lr_reduce = ReduceLROnPlateau(monitor='val_loss', factor=0.3, patience=2, verbose=2, mode='max')

#fit the model with generator
history = model.fit_generator(
      train_generator,
      steps_per_epoch=759,
      epochs=20,
      validation_data=validation_generator,
      validation_steps=5,
      callbacks=[checkpoint,lr_reduce])

model.save('TFXception.h5')

#plot the accuracies and losses at each epochs during training 
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
plt.savefig('TFXception_acc.png', format = 'png')

plt.figure()

plt.plot(epochs, loss, 'bo', label='Training loss')
plt.plot(epochs, val_loss, 'b', label='Validation loss')
plt.title('Training and validation loss')
plt.legend()
plt.savefig('TFXception_loss.png', format = 'png')
