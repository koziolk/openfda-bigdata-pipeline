// TOP reported patient reactions
db.getCollection('drugAdverseEvent').aggregate([
                     { $project: {patientReactions: 1}},
                     { $unwind : "$patientReactions" },
                     { $group: { _id: "$patientReactions", total: { $sum: 1 } } },
                     { $sort: { total: -1 } },
                     { $limit : 20 }
                   ]);
                                         

// TOP reported medical products
db.getCollection('drugAdverseEvent').aggregate([
                     { $project: {medicinalProduct: 1}},
                     { $unwind: "$medicinalProduct" },
                     { $group: { _id: "$medicinalProduct", total: { $sum: 1 } } },
                     { $sort: { total: -1 } },
                     { $limit : 20 }
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
                     
// TOP reported drug brand names
db.getCollection('drugAdverseEvent').aggregate([
                     { $project: {drugBrandNames: 1}},
                     { $unwind : "$drugBrandNames" },
                     { $group: { _id: "$drugBrandNames", total: { $sum: 1 } } },
                     { $sort: { total: -1 } },
                     { $limit : 20 }
                   ]);
                                         
// TOP reported drug substance names
db.getCollection('drugAdverseEvent').aggregate([
                     { $project: {drugSubstanceNames: 1}},
                     { $unwind : "$drugSubstanceNames" },
                     { $group: { _id: "$drugSubstanceNames", total: { $sum: 1 } } },
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
                     
// TOP reported cuntries
db.getCollection('drugAdverseEvent').aggregate([
                     { $project: {country: 1}},
                     { $group: { _id: "$country", total: { $sum: 1 } } },
                     { $sort: { total: -1 } },
                     { $limit : 20 }
                   ]);
                     
// patient sex
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
                     
                     

// TOP 10 years when reported most 
db.getCollection('drugAdverseEvent').aggregate([
                     { $project: { date: { $dateFromString: { dateString: '$receiveDate', format: "%Y%m%d" }}}},
                     { $group: { _id: { $year: "$date" }, total: { $sum: 1 }} },
                     { $sort: { total: -1 } },
                     { $limit : 10 }
                     ]);

// TOP 10 months when reported most 
db.getCollection('drugAdverseEvent').aggregate([
                     { $project: { date: { $dateFromString: { dateString: '$receiveDate', format: "%Y%m%d" }}}},
                     { $group: { _id: { $month: "$date" }, total: { $sum: 1 }} },
                     { $sort: { total: -1 } },
                     { $limit : 10 }
                     ]);

// product group by patitent sex
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
                       
                     
                     
                     