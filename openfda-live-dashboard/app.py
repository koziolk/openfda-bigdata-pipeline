import os
import dash
import pandas as pd
from dash import dcc
from dash import html
import plotly.express as px
from dash.dependencies import Input, Output
import pymongo

# https://codepen.io/LeonGr/pen/yginI.js
# https://codepen.io/chriddyp/pen/bWLwgP.css
# https://codepen.io/ninjakx/pen/bGEpbXo.css

external_stylesheets = []

client = pymongo.MongoClient(os.environ['MONGO_URI'])
database = str(os.environ.get('MONGO_DB', 'openfda'))
graph_refresh_interval = int(os.environ.get('GRAPH_REFRESH_INTERVAL', '30000'))

db = client[database]

app = dash.Dash(__name__, external_stylesheets=external_stylesheets)

app.layout = html.Div([
    html.Div([
        html.H2('FDA Patient reactions'),
        dcc.Graph(id='live-update-graph1'),
        dcc.Interval(
            id='interval-component1',
            interval=graph_refresh_interval,
            n_intervals=0
        )
    ]),
    html.Div([
        html.H2('FDA Reported products'),
        dcc.Graph(id='live-update-graph2'),
        dcc.Interval(
            id='interval-component2',
            interval=graph_refresh_interval,
            n_intervals=0
        )
    ]),
    html.Div([
        html.H2('FDA Death causing products'),
        dcc.Graph(id='live-update-graph4'),
        dcc.Interval(
            id='interval-component4',
            interval=graph_refresh_interval * 4,
            n_intervals=0
        )
    ]),
    html.Div([
        html.H2('FDA Reported countries'),
        dcc.Graph(id='live-update-graph3'),
        dcc.Interval(
            id='interval-component3',
            interval=graph_refresh_interval,
            n_intervals=0
        )
    ]),
    html.Div([
        html.H2('FDA Patient sex'),
        dcc.Graph(id='live-update-graph5'),
        dcc.Interval(
            id='interval-component5',
            interval=graph_refresh_interval,
            n_intervals=0
        )
    ])
])


@app.callback(Output('live-update-graph1', 'figure'),
              Input('interval-component1', 'n_intervals'))
def update_graph_live1(n):

    pipeline = [
        {"$unwind": "$patientReactions"},
        {"$project": {"reaction": "$patientReactions"}},
        {"$group": {"_id": "$reaction", "total": {"$sum": 1}}},
        {"$project": {
            "_id": 0,
            "reaction": "$_id",
            "total": 1
        }},
        {"$sort": {"total": -1}},
        {"$limit": 20}
    ]

    result = db.drugAdverseEvent.aggregate(pipeline, allowDiskUse=True)
    df = pd.DataFrame(list(result))
    fig = px.bar(df, y='total', x='reaction', text_auto='.4s', height=600)

    fig.update_layout(
        title='Patient Reactions',
        yaxis=dict(
            title='Number of occurrences',
            titlefont_size=20,
            tickfont_size=18,
        ),
        xaxis=dict(
            title='Reaction',
            titlefont_size=20,
            tickfont_size=18,
        ),
        barmode='group',
        bargap=0.15,  # gap between bars of adjacent location coordinates.
        bargroupgap=0.1  # gap between bars of the same location coordinate.
    )

    return fig


@app.callback(Output('live-update-graph2', 'figure'),
              Input('interval-component2', 'n_intervals'))
def update_graph_live2(n):

    pipeline = [
        {"$unwind": "$medicinalProduct"},
        {"$project": {"product": {"$substr": [{"$toUpper": "$medicinalProduct"}, 0, 20]}}},
        {"$group": {"_id": "$product", "total": {"$sum": 1}}},
        {"$project": {
            "_id": 0,
            "product": "$_id",
            "total": 1
        }},
        {"$sort": {"total": -1}},
        {"$limit": 20}
    ]

    result = db.drugAdverseEvent.aggregate(pipeline, allowDiskUse=True)
    df = pd.DataFrame(list(result))
    fig = px.bar(df, y='total', x='product', text_auto='.4s', height=600)

    fig.update_layout(
        title='Medical Product',
        yaxis=dict(
            title='Number of occurrences',
            titlefont_size=20,
            tickfont_size=18,
        ),
        xaxis=dict(
            title='Name',
            titlefont_size=20,
            tickfont_size=18,
        ),
        barmode='group',
        bargap=0.15,  # gap between bars of adjacent location coordinates.
        bargroupgap=0.1  # gap between bars of the same location coordinate.
    )

    return fig


@app.callback(Output('live-update-graph3', 'figure'),
              Input('interval-component3', 'n_intervals'))
def update_graph_live3(n):
    pipeline = [
        {"$project": {"country": 1}},
        {"$group": {"_id": "$country", "total": {"$sum": 1}}},
        {"$project": {
            "_id": 0,
            "country": "$_id",
            "total": 1
        }},
        {"$sort": {"total": -1}},
        {"$limit": 20}
    ]

    result = db.drugAdverseEvent.aggregate(pipeline, allowDiskUse=True)
    df = pd.DataFrame(list(result))

    fig = px.pie(df, values='total', names='country', height=600, title="Reported countries")
    fig.update_traces(textposition='inside', textinfo='percent+label')

    return fig


@app.callback(Output('live-update-graph4', 'figure'),
              Input('interval-component4', 'n_intervals'))
def update_graph_live4(n):

    pipeline = [
        {"$unwind": "$medicinalProduct"},
        {"$unwind": "$patientReactions"},
        {"$project": {
            "product": {"$substr": [{"$toUpper": "$medicinalProduct"}, 0, 20]},
            "reaction": {"$toLower": "$patientReactions"}
        }},
        {"$match": {"reaction": "death"}},
        {"$group": {"_id": "$product", "total": {"$sum": 1}}},
        {"$project": {
            "_id": 0,
            "product": "$_id",
            "total": 1
        }},
        {"$sort": {"total": -1}},
        {"$limit": 20}
    ]

    result = db.drugAdverseEvent.aggregate(pipeline, allowDiskUse=True)
    df = pd.DataFrame(list(result))

    fig = px.bar(df, y='total', x='product', text_auto='.4s', height=600)

    fig.update_layout(
        title='Death causing products',
        yaxis=dict(
            title='Number of occurrences',
            titlefont_size=20,
            tickfont_size=18,
        ),
        xaxis=dict(
            title='Product name',
            titlefont_size=20,
            tickfont_size=18,
        ),
        barmode='group',
        bargap=0.15,  # gap between bars of adjacent location coordinates.
        bargroupgap=0.1  # gap between bars of the same location coordinate.
    )

    return fig


@app.callback(Output('live-update-graph5', 'figure'),
              Input('interval-component5', 'n_intervals'))
def update_graph_live5(n):
    pipeline = [
        {"$project": {"patientSex": 1}},
        {"$match": {"patientSex": {"$gte": 0}}},
        {"$group": {"_id": "$patientSex", "total": {"$sum": 1}}},
        {"$project": {
            "_id": 0,
            "total": 1,
            "sex": {
                "$switch": {
                    "branches": [
                        {"case": {"$eq": ["$_id", 1]}, "then": "famale"},
                        {"case": {"$eq": ["$_id", 2]}, "then": "male"}
                    ], "default": "unknown"
                }
            },
        }},
        {"$sort": {"total": -1}},
        {"$limit": 20}
    ]

    result = db.drugAdverseEvent.aggregate(pipeline, allowDiskUse=True)
    df = pd.DataFrame(list(result))

    fig = px.pie(df, values='total', names='sex', height=600, title="Patient sex")
    fig.update_traces(textposition='inside', textinfo='percent+label')

    return fig


if __name__ == '__main__':
    app.run_server(debug=True, host='0.0.0.0')
