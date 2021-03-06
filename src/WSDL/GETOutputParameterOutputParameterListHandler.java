package WSDL;

import java.util.Iterator;

import javax.ws.rs.core.UriInfo;

import WADL.Link;
import WADL.SQLITEController;

public class GETOutputParameterOutputParameterListHandler
{

    private SQLITEController oSQLITEController;
    private OutputParameterModel oSourceOutputParameter;
    private UriInfo		 oApplicationUri;

    GETOutputParameterOutputParameterListHandler(int sourceOutputParameterId, UriInfo applicationUri)
    {
        oSourceOutputParameter = new OutputParameterModel();
        oSourceOutputParameter.setOutputParameterId(sourceOutputParameterId);
        oSQLITEController = new SQLITEController();
        oApplicationUri = applicationUri;
    }

    public OutputParameterModel getOutputParameterList()
    {
        //TODO add authentication if needed

        //get the <resourceName>List from the database
        oSourceOutputParameter = oSQLITEController.getOutputParameterOutputParameterList( oSourceOutputParameter);
        return createHypermediaURIs(oSourceOutputParameter);
    }


    public OutputParameterModel createHypermediaURIs(OutputParameterModel oSourceOutputParameter)
    {
        //create an empty <resourceModel> and populate its linklists with hypermedia links
    	OutputParameterModel oOutputParameter = new OutputParameterModel();

        //add the sibling hypermedia links POST and GET list

        oOutputParameter.getLinkList().add(new Link(String.format("%s%s",oApplicationUri.getBaseUri(),oApplicationUri.getPath()),"List of OutputParameter","GET","Sibling"));
        oOutputParameter.getLinkList().add(new Link(String.format("%s%s",oApplicationUri.getBaseUri(),oApplicationUri.getPath()),"Create new OutputParameter","POST","Sibling"));


        //add the children hypermedia links GET
        Iterator<OutputParameterModel> setIterator = oSourceOutputParameter.getSetOfOutputParameter().iterator();

        while(setIterator.hasNext())
        {
        	OutputParameterModel oNextOutputParameter = new OutputParameterModel();
            oNextOutputParameter = setIterator.next();
            oOutputParameter.getLinkList().add(new Link(String.format("%s%s/%d",oApplicationUri.getBaseUri(),oApplicationUri.getPath(),oNextOutputParameter.getOutputParameterId()),String.format("%s",oNextOutputParameter.getName()), "GET", "Child"));
        }

        String oRelativePath;
        //add the parent hypermedia links POST, GETL
        oRelativePath = oApplicationUri.getPath().replaceAll("multiOutputParameter/","multiOutputParameter/");

        oSourceOutputParameter = oSQLITEController.getOutputParameter(oSourceOutputParameter);
        if(oSourceOutputParameter.getOutputParameter() != null)
        {
            oRelativePath = oRelativePath.replaceAll(String.format("OutputParameter/[0-9]*/OutputParameter"),String.format("OutputParameter/%d/OutputParameter/%d/OutputParameter",oSourceOutputParameter.getOutputParameter().getOutputParameter() ,oSourceOutputParameter.getOutputParameterId()));
        }

        //add the parent's hypermedia links PUT, GET DELETE
        //find last index of "/" in order to cut off to get the parent URI appropriately
        int iLastSlashIndex = String.format("%s%s",oApplicationUri.getBaseUri(),oRelativePath).lastIndexOf("/");
        oOutputParameter.getLinkList().add(new Link(String.format("%s%s",oApplicationUri.getBaseUri(),oRelativePath).substring(0, iLastSlashIndex),"Update","PUT","Parent"));
        oOutputParameter.getLinkList().add(new Link(String.format("%s%s",oApplicationUri.getBaseUri(),oRelativePath).substring(0, iLastSlashIndex),"Read","GET","Parent"));
        oOutputParameter.getLinkList().add(new Link(String.format("%s%s",oApplicationUri.getBaseUri(),oRelativePath).substring(0, iLastSlashIndex),"Delete","DELETE","Parent"));

        return oOutputParameter;
    }

//TODO Extension is needed to support getList on resources that are not related resource of any other.
}