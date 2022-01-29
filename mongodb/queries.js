// TOP reported patient reactions
db.getCollection('drugAdverseEvent').aggregate([
                    {$unwind: "$patientReactions"},
                    {$project: {"reaction": "$patientReactions"}},
                    {$group: {"_id": "$reaction", total: {$sum: 1}}},
                    {$project: {
                            "_id": 0,
                            "reaction": "$_id",
                            "total": 1
                    }},
                    {$sort: {total: -1}},
                    {$limit: 20}
                ]);


// TOP reported medical products
db.getCollection('drugAdverseEvent').aggregate([
                    {$unwind: "$medicinalProduct"},
                    {$project: {"product": {$toUpper: "$medicinalProduct"}}},
                    {$group: {"_id": "$product", total: {$sum: 1}}},
                    {$project: {
                        "_id": 0,
                        "product": {$substr: ["$_id", 0, 20]},
                        "total": 1
                    }},
                    {$sort: {"total": -1}},
                    {$limit: 20}
                ]);

                     
// TOP medical products that are causing death 
db.getCollection('drugAdverseEvent').aggregate([
                     { $unwind: "$medicinalProduct"},
                     { $unwind: "$patientReactions"},
                     { $project: {
                         product: "$medicinalProduct", 
                         reaction: {$toLower: "$patientReactions"} 
                        }
                     },
                     { $match: {reaction : "death"}},
                     { $group: { _id: "$product", total: { $sum: 1 }}},
                     { $project: {
                         product: {"$substr": [{"$toUpper": "$_id"}, 0, 20]}, 
                         total: "$total"
                        }
                     },
                     { $sort: { total: -1 } },
                     { $limit : 20 }
                   ]);
                                      
// TOP reported drug manufacturer names
db.getCollection('drugAdverseEvent').aggregate([
                     { $project: {drugManufacturerNames: 1}},
                     { $unwind : "$drugManufacturerNames" },
                     { $group: { _id: "$drugManufacturerNames", total: { $sum: 1 } } },
                     { $sort: { total: -1 } },
                     { $limit : 20 }
                   ]);
                     
// TOP reported countries
db.getCollection('drugAdverseEvent').aggregate([
                     { $project: {country: 1}},
                     { $group: { _id: "$country", total: { $sum: 1 } } },
                     { $sort: { total: -1 } },
                     { $limit : 20 }
                   ]);
                     
// patient by sex
db.getCollection('drugAdverseEvent').aggregate([
                     { $match: {patientSex : {$gte : 0}}},
                     { $group: { _id: "$patientSex", total: { $sum: 1 } } },
                     {"$project": {
                        sex: {
                            $switch: {
                                branches: [
                                    { case: { $eq: [ "$_id", 1 ] }, then: "famale" },
                                    { case: { $eq: [ "$_id", 2 ] }, then: "male" }
                                ], default: "unknown"
                            }
                        },
                        total: 1,
                        _id: 0
                     }},
                    { $sort: { total: -1 }}
                    ]);


// reported events by month
db.getCollection('drugAdverseEvent').aggregate([
                     { $project: { date: { $dateFromString: { dateString: '$receiveDate', format: "%Y%m%d" }}}},
                     { $group: { _id: { $month: "$date" }, total: { $sum: 1 }} },
                     {"$project": {
                        sex: {
                            $switch: {
                                branches: [
                                    { case: { $eq: [ "$_id", 1 ] }, then: "January" },
                                    { case: { $eq: [ "$_id", 2 ] }, then: "February" },
                                    { case: { $eq: [ "$_id", 3 ] }, then: "March" },
                                    { case: { $eq: [ "$_id", 4 ] }, then: "April" },
                                    { case: { $eq: [ "$_id", 5 ] }, then: "May" },
                                    { case: { $eq: [ "$_id", 6 ] }, then: "June" },
                                    { case: { $eq: [ "$_id", 7 ] }, then: "July" },
                                    { case: { $eq: [ "$_id", 8 ] }, then: "August" },
                                    { case: { $eq: [ "$_id", 9 ] }, then: "September" },
                                    { case: { $eq: [ "$_id", 10 ] }, then: "October" },
                                    { case: { $eq: [ "$_id", 11 ] }, then: "November" },
                                    { case: { $eq: [ "$_id", 12 ] }, then: "December" }
                                ], default: "unknown"
                            }
                        },
                        total: 1,
                        _id: 0
                     }},
                     { $sort: { total: -1 } }
                     ]);

// product group by patient sex
db.getCollection('drugAdverseEvent').aggregate([
                     { $project: { patientSex: 1, medicinalProduct: 1}},
                     { $match: { patientSex : {$gte : 1}}},
                     { $unwind : "$medicinalProduct" },
                     { $group: { _id: { medicinalProduct: "$medicinalProduct", patientSex : "$patientSex"}, total: { $sum: 1 } } },
                     { $sort: { total: -1 } },
                     { $limit : 10 }
                   ]);

// patient reaction group by year
db.getCollection('drugAdverseEvent').aggregate([
                     { $project: {patientReactions: 1, date: { $dateFromString: { dateString: '$receiveDate', format: "%Y%m%d" }} }},
                     { $unwind: "$patientReactions" },
                     { $group: { _id: {patientReactions: "$patientReactions", year :{ $year:"$date" }}, total: { $sum: 1 }}},
                     { $sort: { total: -1 } }
                   ]);