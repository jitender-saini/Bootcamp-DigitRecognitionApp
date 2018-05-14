import sys
import pdb
import joblib
import pickle
import numpy as np
import pandas as pd
#from celery import Celery
from sklearn.metrics import accuracy_score
from sklearn.svm import LinearSVC
from sklearn.tree import DecisionTreeClassifier as DTC
from sklearn.ensemble import RandomForestClassifier

def train_model():
    basepath = ".."
    # basepath = "/var/www/FlaskApplications/SampleApp/api"

    f_train = pd.read_csv(basepath + '/static/Data/train.csv',sep=',')
    train_X = f_train.ix[1:40000,1:]
    train_Y = f_train.ix[1:40000,0]

    test_X = f_train.ix[40000:,1:]
    test_Y = f_train.ix[40000:,0]

    # Create a model
    clf = RandomForestClassifier()
    clf.fit(train_X,train_Y)

    # Store the model
    joblib.dump(clf,basepath + '/static/model.pkl')

    # Predict on test data
    pred_Y = clf.predict(test_X)
    return "Accuracy of trained model is: ",accuracy_score(test_Y,pred_Y) * 100,"%"

def predict_value(test_X):
    basepath = "/var/www/FlaskApplications/SampleApp/api"

    # Load the already created model
    clf = joblib.load(basepath + '/static/model.pkl')

    # Predict on test data
    pred_Y = clf.predict(np.array(test_X))

    return clf.predict(test_X)

train_model()
