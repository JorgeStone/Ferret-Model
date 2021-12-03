/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ourferret;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author youne
 */
public class NCBIData {

    private static Node get_child_node_by_name(Node parent_node, String child_name) {
        Node res = null;
        NodeList child_node_list = parent_node.getChildNodes();
        int list_lgth = child_node_list.getLength();
        for (int i = 0; i < list_lgth; i++) {
            if (child_node_list.item(i).getNodeName().equals(child_name)) {
                res = child_node_list.item(i);
                break;
            }
        }
        return res;
    }

    private static boolean Checker(Node node_test, String type_desired, String heading_desired) {
        boolean type_match = false, heading_match = false;
        if (type_desired.equals("any")) {
            type_match = true;
        }
        if (heading_desired.equals("any")) {
            heading_match = true;
        }
        NodeList comment_tag_list = node_test.getChildNodes();
        int comment_lgth = comment_tag_list.getLength();
        for (int j = 0; j < comment_lgth; j++) {
            String comment_string = comment_tag_list.item(j).getNodeName();
            switch (comment_string) {
                case "Gene-commentary_type":
                    NodeList gene_commentary_type = comment_tag_list.item(j).getChildNodes();
                    for (int k = 0; k < gene_commentary_type.getLength(); k++) {
                        if (gene_commentary_type.item(k).getNodeType() == Node.TEXT_NODE) {
                            type_match = gene_commentary_type.item(k).getNodeValue().equals(type_desired);
                        }
                    }
                    break;
                case "Gene-commentary_heading":
                    NodeList gene_commentary_heading = comment_tag_list.item(j).getChildNodes();
                    for (int k = 0; k < gene_commentary_heading.getLength(); k++) {
                        if (gene_commentary_heading.item(k).getNodeType() == Node.TEXT_NODE) {
                            heading_match = gene_commentary_heading.item(k).getNodeValue().equals(heading_desired);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return type_match & heading_match;
    }

    private static Node Finder(NodeList node_test, String type_desired, String heading_desired, String node_name_to_retrieve) {
        Node res = null;
        int list_lgth = node_test.getLength();
        Node desired_node = null;
        for (int i = 0; i < list_lgth; i++) {
            Node current_node = node_test.item(i);
            if (Checker(current_node, type_desired, heading_desired)) {
                desired_node = current_node;
                break;
            }
        }

        if (desired_node != null) {
            NodeList desired_node_list = desired_node.getChildNodes();
            list_lgth = desired_node_list.getLength();
            for (int i = 0; i < list_lgth; i++) {
                if (desired_node_list.item(i).getNodeName().equals(node_name_to_retrieve)) {
                    res = desired_node_list.item(i);
                }
            }
        }

        return res;
    }

    public static FoundGeneAndRegion get_query_from_gene_ID(String[] gene_list_array, boolean defaultHG) {

        if (gene_list_array.length == 0) {
            return null;
        }

        StringBuffer gene_list = new StringBuffer();
        for (int i = 0; i < gene_list_array.length - 1; i++) {
            gene_list.append(gene_list_array[i]).append(",");
        }
        gene_list.append(gene_list_array[gene_list_array.length - 1]);

        DocumentBuilder docBldr;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setIgnoringComments(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setCoalescing(false);
        StringBuilder found_genes = new StringBuilder();
        ArrayList<LocusModel> queriesArrayList = new ArrayList<>();
        try {
            docBldr = dbf.newDocumentBuilder();
            
            String ncbiEutilsFetchURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=gene&id=" + gene_list + "&retmode=xml";
            org.w3c.dom.Document doc = docBldr.parse(ncbiEutilsFetchURL);
            
            NodeList entrezgeneNodeList = doc.getElementsByTagName("Entrezgene");
            int listLength = entrezgeneNodeList.getLength();
            for (int i = 0; i < listLength; i++) {
                Node currentEntrezNode = entrezgeneNodeList.item(i);
                Node trackNode = get_child_node_by_name(currentEntrezNode, "Entrezgene_track-info");
                Node geneTrackNode = get_child_node_by_name(trackNode, "Gene-track");
                String currentGene = get_child_node_by_name(geneTrackNode, "Gene-track_geneid").getFirstChild().getNodeValue();

                Node subSourceNameNode = get_child_node_by_name(get_child_node_by_name(get_child_node_by_name(get_child_node_by_name(get_child_node_by_name(currentEntrezNode, "Entrezgene_source"),
                        "BioSource"), "BioSource_subtype"), "SubSource"), "SubSource_name");
                String chromosome = subSourceNameNode.getFirstChild().getNodeValue();
                NodeList commentList = get_child_node_by_name(currentEntrezNode, "Entrezgene_comments").getChildNodes();
                Node geneLocationHistoryNode = Finder(commentList, "254", "Gene Location History", "Gene-commentary_comment");
                Node primaryAssemblyNode;
                if (defaultHG) {
                    Node annotationRelease105Node = Finder(geneLocationHistoryNode.getChildNodes(), "254", "Homo sapiens Annotation Release 105", "Gene-commentary_comment");
                    if (annotationRelease105Node == null) {
                        continue;
                    }
                    Node grch37p13Node = Finder(annotationRelease105Node.getChildNodes(), "24", "GRCh37.p13", "Gene-commentary_comment");
                    primaryAssemblyNode = Finder(grch37p13Node.getChildNodes(), "25", "Primary Assembly", "Gene-commentary_comment");
                } else {
                    Node annotationRelease107Node = Finder(geneLocationHistoryNode.getChildNodes(), "254", "Homo sapiens Annotation Release 107", "Gene-commentary_comment");
                    if (annotationRelease107Node == null) {
                        continue;
                    }
                    Node grch38p2Node = Finder(annotationRelease107Node.getChildNodes(), "24", "GRCh38.p2", "Gene-commentary_comment");
                    primaryAssemblyNode = Finder(grch38p2Node.getChildNodes(), "25", "Primary Assembly", "Gene-commentary_comment");
                }
                Node genomicAssemblyNode = Finder(primaryAssemblyNode.getChildNodes(), "1", "any", "Gene-commentary_seqs");
                Node seqLocNode = get_child_node_by_name(genomicAssemblyNode, "Seq-loc");
                Node seqLocIntNode = get_child_node_by_name(seqLocNode, "Seq-loc_int");
                Node seqIntervalNode = get_child_node_by_name(seqLocIntNode, "Seq-interval");
                NodeList sequenceLocationNodeList = seqIntervalNode.getChildNodes();
                int listLocationLength = sequenceLocationNodeList.getLength();
                String startPos = new String(), endPos = new String();
                for (int j = 0; j < listLocationLength; j++) {
                    Node currentNode = sequenceLocationNodeList.item(j);
                    if (currentNode.getNodeName().equals("Seq-interval_from")) {
                        startPos = currentNode.getFirstChild().getNodeValue();
                    }
                    if (currentNode.getNodeName().equals("Seq-interval_to")) {
                        endPos = currentNode.getFirstChild().getNodeValue();
                    }
                }
                if (!chromosome.equals("X") && !chromosome.equals("Y") && !chromosome.equals("MT")) {
                    queriesArrayList.add(new LocusModel(chromosome, Integer.parseInt(startPos), Integer.parseInt(endPos)));
                    found_genes.append(currentGene).append(",");
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return null;
        }

        LocusModel[] queries_found = queriesArrayList.toArray(new LocusModel[queriesArrayList.size()]);

        found_genes.deleteCharAt(found_genes.length()-1);
        return new FoundGeneAndRegion(found_genes.toString(), queries_found, queries_found.length == gene_list_array.length);
    }

    public static FoundGeneAndRegion get_query_from_gene_name(String[] gene_list_array, boolean defaultHG) {
        if (gene_list_array.length == 0) {
            return null;
        }
        StringBuffer gene_list = new StringBuffer();
        for (int i = 0; i < gene_list_array.length - 1; i++) {
            gene_list.append(gene_list_array[i]).append("[GENE]+OR+");
        }
        gene_list.append(gene_list_array[gene_list_array.length - 1]).append("[GENE]");

        ArrayList<LocusModel> query_array_list = new ArrayList<>();
        String gene_string = new String();
        StringBuilder found_genes = new StringBuilder();
        DocumentBuilder docBldr;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setIgnoringComments(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setCoalescing(false);
        try {
            docBldr = dbf.newDocumentBuilder();

            int listLength;
            String ncbiEutilsSearchURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=gene&term=" + gene_list + "+AND+Homo+sapiens[ORGN]";
            org.w3c.dom.Document doc = docBldr.parse(ncbiEutilsSearchURL);
            Node resultsNode = doc.getElementsByTagName("eSearchResult").item(0);
            Node idListNode = get_child_node_by_name(resultsNode, "IdList");
            NodeList idsNodeList = idListNode.getChildNodes();
            listLength = idsNodeList.getLength();
            if (listLength == 0) { // nothing found so return null; might have to do something more advanced here later
                return null;
            }
            StringBuffer geneListIDBuffer = new StringBuffer();
            for (int i = 0; i < listLength; i++) {
                geneListIDBuffer.append(idsNodeList.item(i).getFirstChild().getNodeValue());
                geneListIDBuffer.append(',');
            }
            geneListIDBuffer.trimToSize();
            geneListIDBuffer = geneListIDBuffer.deleteCharAt(geneListIDBuffer.length() - 1);
            gene_string = geneListIDBuffer.toString();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return null;
        }

        try {
            // see if there are more than 500 in the list
            String ncbiEutilsFetchURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=gene&id=" + gene_string + "&retmode=xml";
            docBldr = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = docBldr.parse(ncbiEutilsFetchURL);
            // Do the next steps in a loop for each gene id
            NodeList entrezgeneNodeList = doc.getElementsByTagName("Entrezgene");
            int listLength = entrezgeneNodeList.getLength();
            for (int i = 0; i < listLength; i++) {
                Node currentEntrezNode = entrezgeneNodeList.item(i);
                Node trackNode = get_child_node_by_name(currentEntrezNode, "Entrezgene_track-info");
                Node geneTrackNode = get_child_node_by_name(trackNode, "Gene-track");
                Node geneStatusNode = get_child_node_by_name(geneTrackNode, "Gene-track_status");
                NamedNodeMap attributes = geneStatusNode.getAttributes();
                if (attributes.getLength() != 1) {
                    continue;
                }
                if (!attributes.item(0).getNodeValue().equals("live")) {
                    continue;
                }
                Node geneNode = get_child_node_by_name(currentEntrezNode, "Entrezgene_gene");
                Node geneRefNode = get_child_node_by_name(geneNode, "Gene-ref");
                Node geneRefLocusNode = get_child_node_by_name(geneRefNode, "Gene-ref_locus");
                String geneNameFound = geneRefLocusNode.getFirstChild().getNodeValue();
                if (!Arrays.asList(gene_list_array).contains(geneNameFound.toUpperCase())) {
                    continue;
                }

                // This is a horrible line. Mainly, this is horrible because I don't think nesting makes sense here but if not, would
                // result in a lot of confusing variable names for nodes
                Node subSourceNodeForChr = get_child_node_by_name(get_child_node_by_name(get_child_node_by_name(get_child_node_by_name(get_child_node_by_name(currentEntrezNode, "Entrezgene_source"),
                        "BioSource"), "BioSource_subtype"), "SubSource"), "SubSource_name");
                String chromosome = subSourceNodeForChr.getFirstChild().getNodeValue();
                NodeList commentList = get_child_node_by_name(currentEntrezNode, "Entrezgene_comments").getChildNodes();
                Node geneLocationHistoryNode = Finder(commentList, "254", "Gene Location History", "Gene-commentary_comment");
                Node primaryAssemblyNode;
                if (defaultHG) {
                    Node annotationRelease105Node = Finder(geneLocationHistoryNode.getChildNodes(), "254", "Homo sapiens Annotation Release 105", "Gene-commentary_comment");
                    if (annotationRelease105Node == null) {
                        continue;
                    }
                    Node grch37p13Node = Finder(annotationRelease105Node.getChildNodes(), "24", "GRCh37.p13", "Gene-commentary_comment");
                    primaryAssemblyNode = Finder(grch37p13Node.getChildNodes(), "25", "Primary Assembly", "Gene-commentary_comment");
                } else {
                    Node annotationRelease107Node = Finder(geneLocationHistoryNode.getChildNodes(), "254", "Homo sapiens Annotation Release 107", "Gene-commentary_comment");
                    if (annotationRelease107Node == null) {
                        continue;
                    }
                    Node grch38p2Node = Finder(annotationRelease107Node.getChildNodes(), "24", "GRCh38.p2", "Gene-commentary_comment");
                    primaryAssemblyNode = Finder(grch38p2Node.getChildNodes(), "25", "Primary Assembly", "Gene-commentary_comment");
                }
                Node genomicAssemblyNode = Finder(primaryAssemblyNode.getChildNodes(), "1", "any", "Gene-commentary_seqs");
                Node seqLocNode = get_child_node_by_name(genomicAssemblyNode, "Seq-loc");
                Node seqLocIntNode = get_child_node_by_name(seqLocNode, "Seq-loc_int");
                Node seqIntervalNode = get_child_node_by_name(seqLocIntNode, "Seq-interval");
                NodeList sequenceLocationNodeList = seqIntervalNode.getChildNodes();
                int listLocationLength = sequenceLocationNodeList.getLength();
                String startPos = new String(), endPos = new String();
                for (int j = 0; j < listLocationLength; j++) {
                    Node currentNode = sequenceLocationNodeList.item(j);
                    if (currentNode.getNodeName().equals("Seq-interval_from")) {
                        startPos = currentNode.getFirstChild().getNodeValue();
                    }
                    if (currentNode.getNodeName().equals("Seq-interval_to")) {
                        endPos = currentNode.getFirstChild().getNodeValue();
                    }
                }
                if (!chromosome.equals("X") && !chromosome.equals("Y") && !chromosome.equals("MT")) {
                    LocusModel locusm = new LocusModel(chromosome,Integer.parseInt(startPos),Integer.parseInt(endPos));
                    query_array_list.add(locusm);
                    found_genes.append(geneNameFound).append(",");
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return null;
        }

        LocusModel[] queries_found = query_array_list.toArray(new LocusModel[query_array_list.size()]);

        found_genes.deleteCharAt(found_genes.length() - 1);
        return new FoundGeneAndRegion(found_genes.toString(), queries_found, query_array_list.size() == gene_list_array.length);
    }

}
