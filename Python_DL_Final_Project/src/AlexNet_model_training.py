import tensorflow.compat.v1 as tf
from tensorflow.compat.v1.keras import layers
from tensorflow.compat.v1.keras import models
from tensorflow.keras.callbacks import ModelCheckpoint, ReduceLROnPlateau
from tensorflow.compat.v1.keras import backend as K
import os

K.clear_session()

#target directory
dataset_dir = 'OCT2017/'
train_dir = os.path.join(dataset_dir, 'train')
val_dir = os.path.join(dataset_dir, 'val')
 
#build model structure
model = models.Sequential()
#conv block
model.add(layers.Conv2D(96, (11, 11), strides=(1, 1), input_shape=(112, 112, 3), padding='same', activation='relu',
                 kernel_initializer='uniform'))
model.add(layers.MaxPooling2D(pool_size=(3, 3), strides=(2, 2)))
model.add(layers.Conv2D(256, (5, 5), strides=(1, 1), padding='same', activation='relu', kernel_initializer='uniform'))
model.add(layers.MaxPooling2D(pool_size=(3, 3), strides=(2, 2)))
model.add(layers.Conv2D(384, (3, 3), strides=(1, 1), padding='same', activation='relu', kernel_initializer='uniform'))
model.add(layers.Conv2D(384, (3, 3), strides=(1, 1), padding='same', activation='relu', kernel_initializer='uniform'))
model.add(layers.Conv2D(256, (3, 3), strides=(1, 1), padding='same', activation='relu', kernel_initializer='uniform'))
model.add(layers.MaxPooling2D(pool_size=(3, 3), strides=(2, 2)))

#full connected block
model.add(layers.Flatten())
model.add(layers.Dense(4096, activation='relu'))
model.add(layers.Dropout(0.5))
model.add(layers.Dense(4096, activation='relu'))
model.add(layers.Dropout(0.5))
model.add(layers.Dense(4, activation='softmax'))

#compile model
model.compile(loss='categorical_crossentropy', optimizer='sgd', metrics=['accuracy'])
model.summary()

from tensorflow.compat.v1.keras import optimizers

from tensorflow.compat.v1.keras.preprocessing.image import ImageDataGenerator

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
checkpoint = ModelCheckpoint(filepath='Alexnet_best_weights.hdf5', save_best_only=True, save_weights_only=True)
lr_reduce = ReduceLROnPlateau(monitor='val_loss', factor=0.3, patience=2, verbose=2, mode='max')

#fit the model with generator
history = model.fit_generator(
      train_generator,
      steps_per_epoch=759,
      epochs=20,
      validation_data=validation_generator,
      validation_steps=5,
      callbacks=[checkpoint,lr_reduce])

model.save('Alexnet.h5')

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
plt.savefig('Alexnet_acc.png', format = 'png')
plt.show()

plt.figure()

plt.plot(epochs, loss, 'bo', label='Training loss')
plt.plot(epochs, val_loss, 'b', label='Validation loss')
plt.title('Training and validation loss')
plt.legend()
plt.savefig('Alexnet_loss.png', format = 'png')
plt.show()