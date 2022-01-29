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

external_stylesheets = ['https://codepen.io/chriddyp/pen/bWLwgP.css']

# env variables
client = pymongo.MongoClient(os.environ['MONGO_URI'])
database = str(os.environ.get('MONGO_DB', 'openfda'))
graph_refresh_interval = int(os.environ.get('GRAPH_REFRESH_INTERVAL', '30000'))

# database
db = client[database]

app = dash.Dash(__name__, external_stylesheets=external_stylesheets)

app.layout = html.Div([
    html.Div([
        html.H2('Patient reactions'),
        dcc.Graph(id='live-update-graph1'),
        dcc.Interval(
            id='interval-component1',
            interval=graph_refresh_interval,
            n_intervals=0
        )
    ]),
    html.Div([
        html.H2('Medical products'),
        dcc.Graph(id='live-update-graph2'),
        dcc.Interval(
            id='interval-component2',
            interval=graph_refresh_interval,
            n_intervals=0
        )
    ]),
    html.Div([
        html.H2('Reactions by month'),
        dcc.Graph(id='live-update-graph3'),
        dcc.Interval(
            id='interval-component3',
            interval=graph_refresh_interval,
            n_intervals=0
        )
    ]),
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
        yaxis=dict(
            title='Number of events',
            titlefont_size=20,
            tickfont_size=18
        ),
        xaxis=dict(
            title='Reaction',
            titlefont_size=20,
            tickfont_size=18
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
        {"$project": {"product": {"$toUpper": "$medicinalProduct"}}},
        {"$group": {"_id": "$product", "total": {"$sum": 1}}},
        {"$project": {
            "_id": 0,
            "product": {"$substr": ["$_id", 0, 20]},
            "total": 1
        }},
        {"$sort": {"total": -1}},
        {"$limit": 20}
    ]

    result = db.drugAdverseEvent.aggregate(pipeline, allowDiskUse=True)
    df = pd.DataFrame(list(result))
    fig = px.bar(df, y='total', x='product', text_auto='.4s', height=600)

    fig.update_layout(
        yaxis=dict(
            title='Number of events',
            titlefont_size=20,
            tickfont_size=18
        ),
        xaxis=dict(
            title='Name',
            titlefont_size=20,
            tickfont_size=18
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
        {"$project": {"date": {"$dateFromString": {"dateString": "$receiveDate", "format": "%Y%m%d"}}}},
        {"$group": {"_id": {"$month": "$date"}, "total": {"$sum": 1}}},
        {"$project": {
            "month": {
                "$switch": {
                    "branches": [
                        {"case": { "$eq": ["$_id", 1]}, "then": "January"},
                        {"case": { "$eq": ["$_id", 2]}, "then": "February"},
                        {"case": { "$eq": ["$_id", 3]}, "then": "March"},
                        {"case": { "$eq": ["$_id", 4]}, "then": "April"},
                        {"case": { "$eq": ["$_id", 5]}, "then": "May"},
                        {"case": { "$eq": ["$_id", 6]}, "then": "June"},
                        {"case": { "$eq": ["$_id", 7]}, "then": "July"},
                        {"case": { "$eq": ["$_id", 8]}, "then": "August"},
                        {"case": { "$eq": ["$_id", 9]}, "then": "September"},
                        {"case": { "$eq": ["$_id", 10]}, "then": "October"},
                        {"case": { "$eq": ["$_id", 11]}, "then": "November"},
                        {"case": { "$eq": ["$_id", 12]}, "then": "December"}
                    ], "default": "unknown"
                }
            },
            "total": 1,
            "_id": 0
        }},
        {"$sort": {"total": -1}}
    ]

    result = db.drugAdverseEvent.aggregate(pipeline, allowDiskUse=True)
    df = pd.DataFrame(list(result))

    fig = px.bar(df, y='total', x='month', text_auto='.4s', height=600)

    fig.update_layout(
        yaxis=dict(
            title='Number of events',
            titlefont_size=20,
            tickfont_size=18
        ),
        xaxis=dict(
            title='Month',
            titlefont_size=20,
            tickfont_size=18
        ),
        barmode='group',
        bargap=0.15,  # gap between bars of adjacent location coordinates.
        bargroupgap=0.1  # gap between bars of the same location coordinate.
    )

    return fig


if __name__ == '__main__':
    app.run_server(debug=True, host='0.0.0.0')
